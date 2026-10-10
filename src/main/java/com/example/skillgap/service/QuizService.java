package com.example.skillgap.service;

import com.example.skillgap.dto.request.QuizAnswerDto;
import com.example.skillgap.dto.request.QuizSubmitRequest;
import com.example.skillgap.dto.response.*;
import com.example.skillgap.entity.*;
import com.example.skillgap.exception.BadRequestException;
import com.example.skillgap.exception.ResourceNotFoundException;
import com.example.skillgap.repository.*;
import com.example.skillgap.util.ScoringUtils;
import com.example.skillgap.util.SkillGapCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Core business engine managing objective quiz generation, answer verification,
 * scoring calculations, proficiency level evaluations, and persistence.
 */
@Service
public class QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);

    private final RoleRepository roleRepository;
    private final RoleRequirementRepository roleRequirementRepository;
    private final QuestionRepository questionRepository;
    private final StudentRepository studentRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizResultRepository quizResultRepository;
    private final StudentSkillRepository studentSkillRepository;

    public QuizService(RoleRepository roleRepository,
                       RoleRequirementRepository roleRequirementRepository,
                       QuestionRepository questionRepository,
                       StudentRepository studentRepository,
                       QuizAttemptRepository quizAttemptRepository,
                       QuizResultRepository quizResultRepository,
                       StudentSkillRepository studentSkillRepository) {
        this.roleRepository = roleRepository;
        this.roleRequirementRepository = roleRequirementRepository;
        this.questionRepository = questionRepository;
        this.studentRepository = studentRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.quizResultRepository = quizResultRepository;
        this.studentSkillRepository = studentSkillRepository;
    }

    /**
     * Generates a balanced role-based quiz ensuring every required skill for the role
     * is represented with a blend of difficulty levels.
     * Note: Does NOT include correct answers in the response.
     *
     * @param roleId the career role ID
     * @return QuizResponse containing sanitized questions for the client
     */
    @Transactional(readOnly = true)
    public QuizResponse generateQuiz(Long roleId) {
        try {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> {
                        System.err.println("QuizService: Role not found for quiz generation, ID: " + roleId);
                        return new ResourceNotFoundException("Role not found with ID: " + roleId);
                    });

            List<RoleRequirement> requirements = roleRequirementRepository.findByRole_RoleId(roleId);
            if (requirements.isEmpty()) {
                System.err.println("QuizService: No skills mapped to role ID: " + roleId);
                throw new BadRequestException("Selected role does not have any skill requirements configured.");
            }

            List<QuizQuestionResponse> selectedQuestions = new ArrayList<>();

            for (RoleRequirement req : requirements) {
                Long skillId = req.getSkill().getSkillId();
                String skillName = req.getSkill().getSkillName();
                List<Question> skillQuestions = questionRepository.findBySkill_SkillId(skillId);

                if (skillQuestions.isEmpty()) {
                    logger.warn("No questions found in question bank for skill: {}", skillName);
                    continue;
                }

                // Balance question selection: try to pick ~5 questions per skill with easy/medium/hard mix
                List<Question> picked = pickBalancedQuestions(skillQuestions, 5);

                for (Question q : picked) {
                    selectedQuestions.add(QuizQuestionResponse.builder()
                            .questionId(q.getQuestionId())
                            .skillId(skillId)
                            .skillName(skillName)
                            .questionText(q.getQuestionText())
                            .optionA(q.getOptionA())
                            .optionB(q.getOptionB())
                            .optionC(q.getOptionC())
                            .optionD(q.getOptionD())
                            .difficulty(q.getDifficulty())
                            .topic(q.getTopic())
                            .build());
                }
            }

            // Shuffle questions to provide random presentation
            Collections.shuffle(selectedQuestions);

            return QuizResponse.builder()
                    .roleId(role.getRoleId())
                    .roleName(role.getRoleName())
                    .totalQuestions(selectedQuestions.size())
                    .questions(selectedQuestions)
                    .build();
        } catch (ResourceNotFoundException | BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("QuizService: Error in generateQuiz: " + ex.getMessage());
            logger.error("Failed to generate quiz for role ID: {}", roleId, ex);
            throw new RuntimeException("Error generating quiz", ex);
        }
    }

    /**
     * Submits student answers, looks up authoritative correct answers from the database,
     * calculates per-skill and overall scores, assigns proficiency levels 1-5, and updates records.
     *
     * @param request the quiz submission payload
     * @return QuizResultResponse with detailed results and skill gap analysis
     */
    @Transactional
    public QuizResultResponse submitQuiz(QuizSubmitRequest request) {
        try {
            Student student = studentRepository.findById(request.getStudentId())
                    .orElseThrow(() -> {
                        System.err.println("QuizService: Student not found for quiz submission: " + request.getStudentId());
                        return new ResourceNotFoundException("Student not found with ID: " + request.getStudentId());
                    });

            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> {
                        System.err.println("QuizService: Role not found for quiz submission: " + request.getRoleId());
                        return new ResourceNotFoundException("Role not found with ID: " + request.getRoleId());
                    });

            List<RoleRequirement> requirements = roleRequirementRepository.findByRole_RoleId(request.getRoleId());
            Map<Long, Integer> requiredLevelMap = requirements.stream()
                    .collect(Collectors.toMap(r -> r.getSkill().getSkillId(), RoleRequirement::getRequiredLevel));

            // Extract question IDs from answers
            List<Long> questionIds = request.getAnswers().stream()
                    .map(QuizAnswerDto::getQuestionId)
                    .toList();

            List<Question> questionsFromDb = questionRepository.findAllById(questionIds);
            Map<Long, Question> questionMap = questionsFromDb.stream()
                    .collect(Collectors.toMap(Question::getQuestionId, q -> q));

            // Map student answers by question ID
            Map<Long, String> answerMap = request.getAnswers().stream()
                    .collect(Collectors.toMap(QuizAnswerDto::getQuestionId, QuizAnswerDto::getSelectedOption, (first, second) -> second));

            // Aggregation containers per skill
            Map<Long, Integer> skillTotalMap = new HashMap<>();
            Map<Long, Integer> skillCorrectMap = new HashMap<>();
            Map<Long, Skill> skillEntityMap = new HashMap<>();
            Map<Long, Set<String>> skillWrongTopicsMap = new HashMap<>();

            for (Map.Entry<Long, String> entry : answerMap.entrySet()) {
                Long qId = entry.getKey();
                String selected = entry.getValue();
                Question q = questionMap.get(qId);

                if (q == null) {
                    logger.warn("Question ID {} not found in database during submission", qId);
                    continue;
                }

                Long skillId = q.getSkill().getSkillId();
                skillEntityMap.put(skillId, q.getSkill());
                skillTotalMap.put(skillId, skillTotalMap.getOrDefault(skillId, 0) + 1);
                skillWrongTopicsMap.putIfAbsent(skillId, new LinkedHashSet<>());

                boolean isCorrect = q.getCorrectOption().trim().equalsIgnoreCase(selected.trim());
                if (isCorrect) {
                    skillCorrectMap.put(skillId, skillCorrectMap.getOrDefault(skillId, 0) + 1);
                } else {
                    if (q.getTopic() != null && !q.getTopic().isBlank()) {
                        skillWrongTopicsMap.get(skillId).add(q.getTopic().trim());
                    }
                }
            }

            // Calculate skill scores and create QuizResult records
            List<QuizResult> resultsToSave = new ArrayList<>();
            List<SkillResultDto> skillResultDtos = new ArrayList<>();
            double totalScoreSum = 0.0;
            int evaluatedSkillCount = 0;

            for (Map.Entry<Long, Integer> entry : skillTotalMap.entrySet()) {
                Long skillId = entry.getKey();
                int totalQuestions = entry.getValue();
                int correctCount = skillCorrectMap.getOrDefault(skillId, 0);

                double scorePercentage = ScoringUtils.calculateScorePercentage(correctCount, totalQuestions);
                int level = ScoringUtils.convertScoreToProficiencyLevel(scorePercentage);
                int reqLevel = requiredLevelMap.getOrDefault(skillId, 4);
                int gap = SkillGapCalculator.calculateGap(reqLevel, level);

                Skill skill = skillEntityMap.get(skillId);
                List<String> wrongTopics = new ArrayList<>(skillWrongTopicsMap.getOrDefault(skillId, Collections.emptySet()));

                // Update or create StudentSkill
                updateStudentSkillProficiency(student, skill, level);

                SkillResultDto dto = SkillResultDto.builder()
                        .skillId(skillId)
                        .skill(skill.getSkillName())
                        .skillName(skill.getSkillName())
                        .score(scorePercentage)
                        .level(level)
                        .requiredLevel(reqLevel)
                        .gap(gap)
                        .wrongTopics(wrongTopics)
                        .priority(1)
                        .build();

                skillResultDtos.add(dto);
                totalScoreSum += scorePercentage;
                evaluatedSkillCount++;
            }

            // Prioritize skills by gap descending, then lower score, then weak topics
            prioritizeSkillResults(skillResultDtos);

            double overallScore = evaluatedSkillCount > 0
                    ? Math.round((totalScoreSum / evaluatedSkillCount) * 10.0) / 10.0
                    : 0.0;
            int overallLevel = ScoringUtils.convertScoreToProficiencyLevel(overallScore);

            // Persist QuizAttempt
            QuizAttempt attempt = QuizAttempt.builder()
                    .student(student)
                    .role(role)
                    .attemptDate(LocalDateTime.now())
                    .totalScore(overallScore)
                    .build();
            QuizAttempt savedAttempt = quizAttemptRepository.save(attempt);

            // Persist QuizResult records
            for (SkillResultDto dto : skillResultDtos) {
                Skill skill = skillEntityMap.get(dto.getSkillId());
                int totalQ = skillTotalMap.getOrDefault(dto.getSkillId(), 0);
                int correctQ = skillCorrectMap.getOrDefault(dto.getSkillId(), 0);

                QuizResult result = QuizResult.builder()
                        .attempt(savedAttempt)
                        .skill(skill)
                        .correctCount(correctQ)
                        .totalQuestions(totalQ)
                        .level(dto.getLevel())
                        .build();
                resultsToSave.add(result);
            }
            quizResultRepository.saveAll(resultsToSave);

            return QuizResultResponse.builder()
                    .attemptId(savedAttempt.getAttemptId())
                    .roleId(role.getRoleId())
                    .roleName(role.getRoleName())
                    .overallScore(overallScore)
                    .overallLevel(overallLevel)
                    .attemptDate(savedAttempt.getAttemptDate())
                    .skills(skillResultDtos)
                    .build();
        } catch (ResourceNotFoundException | BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("QuizService: Error in submitQuiz: " + ex.getMessage());
            logger.error("Failed to submit and evaluate quiz", ex);
            throw new RuntimeException("Error submitting quiz", ex);
        }
    }

    /**
     * Retrieves the comprehensive evaluation outcome of a previously completed quiz attempt.
     *
     * @param attemptId the attempt primary key
     * @return QuizResultResponse DTO
     */
    @Transactional(readOnly = true)
    public QuizResultResponse getQuizResult(Long attemptId) {
        try {
            QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                    .orElseThrow(() -> {
                        System.err.println("QuizService: Attempt not found with ID: " + attemptId);
                        return new ResourceNotFoundException("Quiz attempt not found with ID: " + attemptId);
                    });

            List<QuizResult> results = quizResultRepository.findByAttempt_AttemptId(attemptId);
            List<RoleRequirement> requirements = roleRequirementRepository.findByRole_RoleId(attempt.getRole().getRoleId());
            Map<Long, Integer> requiredMap = requirements.stream()
                    .collect(Collectors.toMap(r -> r.getSkill().getSkillId(), RoleRequirement::getRequiredLevel));

            List<SkillResultDto> skillDtos = new ArrayList<>();
            for (QuizResult r : results) {
                double score = ScoringUtils.calculateScorePercentage(r.getCorrectCount(), r.getTotalQuestions());
                int reqLevel = requiredMap.getOrDefault(r.getSkill().getSkillId(), 4);
                int gap = SkillGapCalculator.calculateGap(reqLevel, r.getLevel());

                skillDtos.add(SkillResultDto.builder()
                        .skillId(r.getSkill().getSkillId())
                        .skill(r.getSkill().getSkillName())
                        .skillName(r.getSkill().getSkillName())
                        .score(score)
                        .level(r.getLevel())
                        .requiredLevel(reqLevel)
                        .gap(gap)
                        .wrongTopics(List.of()) // historical summary
                        .priority(1)
                        .build());
            }

            prioritizeSkillResults(skillDtos);

            return QuizResultResponse.builder()
                    .attemptId(attempt.getAttemptId())
                    .roleId(attempt.getRole().getRoleId())
                    .roleName(attempt.getRole().getRoleName())
                    .overallScore(attempt.getTotalScore())
                    .overallLevel(ScoringUtils.convertScoreToProficiencyLevel(attempt.getTotalScore()))
                    .attemptDate(attempt.getAttemptDate())
                    .skills(skillDtos)
                    .build();
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("QuizService: Error in getQuizResult: " + ex.getMessage());
            logger.error("Failed to retrieve quiz result for attempt ID: {}", attemptId, ex);
            throw new RuntimeException("Error fetching quiz result", ex);
        }
    }

    /**
     * Updates or creates the assessed proficiency for a student in the student_skills table.
     *
     * @param student     the student entity
     * @param skill       the evaluated skill entity
     * @param proficiency the newly computed level (1 to 5)
     */
    private void updateStudentSkillProficiency(Student student, Skill skill, int proficiency) {
        try {
            Optional<StudentSkill> existing = studentSkillRepository
                    .findByStudent_StudentIdAndSkill_SkillId(student.getStudentId(), skill.getSkillId());

            if (existing.isPresent()) {
                StudentSkill ss = existing.get();
                ss.setProficiency(proficiency);
                studentSkillRepository.save(ss);
            } else {
                StudentSkill newSkill = StudentSkill.builder()
                        .student(student)
                        .skill(skill)
                        .proficiency(proficiency)
                        .build();
                studentSkillRepository.save(newSkill);
            }
        } catch (Exception ex) {
            System.err.println("QuizService: Error updating student skill proficiency: " + ex.getMessage());
            logger.error("Failed to update student skill proficiency", ex);
        }
    }

    /**
     * Prioritizes skill results by largest gap descending, then lower score, then weak topics.
     *
     * @param skillDtos list of skill result DTOs
     */
    private void prioritizeSkillResults(List<SkillResultDto> skillDtos) {
        try {
            skillDtos.sort((a, b) -> {
                int gapCompare = Integer.compare(b.getGap(), a.getGap()); // Largest gap first
                if (gapCompare != 0) return gapCompare;

                double scoreA = a.getScore() != null ? a.getScore() : 0.0;
                double scoreB = b.getScore() != null ? b.getScore() : 0.0;
                int scoreCompare = Double.compare(scoreA, scoreB); // Lower score first
                if (scoreCompare != 0) return scoreCompare;

                int topicsA = a.getWrongTopics() != null ? a.getWrongTopics().size() : 0;
                int topicsB = b.getWrongTopics() != null ? b.getWrongTopics().size() : 0;
                return Integer.compare(topicsB, topicsA); // More wrong topics first
            });

            for (int i = 0; i < skillDtos.size(); i++) {
                skillDtos.get(i).setPriority(i + 1);
            }
        } catch (Exception ex) {
            System.err.println("QuizService: Error prioritizing skill results: " + ex.getMessage());
            logger.error("Failed to prioritize skill results", ex);
        }
    }

    /**
     * Picks a balanced selection of questions across available difficulties.
     *
     * @param allQuestions list of questions for a skill
     * @param targetCount  desired number of questions (e.g. 5)
     * @return list of selected questions
     */
    private List<Question> pickBalancedQuestions(List<Question> allQuestions, int targetCount) {
        try {
            if (allQuestions.size() <= targetCount) {
                return new ArrayList<>(allQuestions);
            }

            List<Question> easy = new ArrayList<>();
            List<Question> medium = new ArrayList<>();
            List<Question> hard = new ArrayList<>();

            for (Question q : allQuestions) {
                if ("EASY".equalsIgnoreCase(q.getDifficulty())) easy.add(q);
                else if ("MEDIUM".equalsIgnoreCase(q.getDifficulty())) medium.add(q);
                else hard.add(q);
            }

            Collections.shuffle(easy);
            Collections.shuffle(medium);
            Collections.shuffle(hard);

            List<Question> result = new ArrayList<>();
            // Distribute across easy (2), medium (2), hard (1) or whatever available
            if (!easy.isEmpty()) result.add(easy.remove(0));
            if (!medium.isEmpty()) result.add(medium.remove(0));
            if (!hard.isEmpty()) result.add(hard.remove(0));
            if (!easy.isEmpty() && result.size() < targetCount) result.add(easy.remove(0));
            if (!medium.isEmpty() && result.size() < targetCount) result.add(medium.remove(0));

            // Fill remainder if needed
            List<Question> remaining = new ArrayList<>();
            remaining.addAll(easy);
            remaining.addAll(medium);
            remaining.addAll(hard);
            Collections.shuffle(remaining);

            while (result.size() < targetCount && !remaining.isEmpty()) {
                result.add(remaining.remove(0));
            }

            return result;
        } catch (Exception ex) {
            System.err.println("QuizService: Error picking balanced questions: " + ex.getMessage());
            logger.error("Failed to pick balanced questions", ex);
            return allQuestions.subList(0, Math.min(targetCount, allQuestions.size()));
        }
    }
}
