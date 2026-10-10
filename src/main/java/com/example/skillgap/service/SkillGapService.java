package com.example.skillgap.service;

import com.example.skillgap.dto.response.SkillGapResponse;
import com.example.skillgap.entity.*;
import com.example.skillgap.exception.ResourceNotFoundException;
import com.example.skillgap.repository.*;
import com.example.skillgap.util.ScoringUtils;
import com.example.skillgap.util.SkillGapCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service managing skill gap calculations, benchmark comparisons, and deterministic prioritization.
 */
@Service
public class SkillGapService {

    private static final Logger logger = LoggerFactory.getLogger(SkillGapService.class);

    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final RoleRequirementRepository roleRequirementRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizResultRepository quizResultRepository;

    public SkillGapService(StudentRepository studentRepository,
                           RoleRepository roleRepository,
                           RoleRequirementRepository roleRequirementRepository,
                           StudentSkillRepository studentSkillRepository,
                           QuizAttemptRepository quizAttemptRepository,
                           QuizResultRepository quizResultRepository) {
        this.studentRepository = studentRepository;
        this.roleRepository = roleRepository;
        this.roleRequirementRepository = roleRequirementRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.quizResultRepository = quizResultRepository;
    }

    /**
     * Computes the prioritized skill gaps for a student comparing their target role requirements
     * against their objectively assessed quiz proficiencies.
     *
     * @param studentId the student primary key
     * @return prioritized list of SkillGapResponse items
     */
    @Transactional(readOnly = true)
    public List<SkillGapResponse> getSkillGaps(Long studentId) {
        try {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> {
                        System.err.println("SkillGapService: Student not found with ID: " + studentId);
                        return new ResourceNotFoundException("Student not found with ID: " + studentId);
                    });

            // Determine target role: check latest quiz attempt first, fallback to student's careerGoal or default
            Role targetRole = resolveTargetRole(student);
            if (targetRole == null) {
                logger.warn("No career role could be resolved for student ID: {}", studentId);
                return List.of();
            }

            List<RoleRequirement> requirements = roleRequirementRepository.findByRole_RoleId(targetRole.getRoleId());
            List<StudentSkill> studentSkills = studentSkillRepository.findByStudent_StudentId(studentId);
            Map<Long, Integer> studentSkillProficiencyMap = studentSkills.stream()
                    .collect(Collectors.toMap(ss -> ss.getSkill().getSkillId(), StudentSkill::getProficiency, (a, b) -> b));

            // Get latest quiz results for score percentages
            Optional<QuizAttempt> latestAttempt = quizAttemptRepository
                    .findFirstByStudent_StudentIdAndRole_RoleIdOrderByAttemptDateDesc(studentId, targetRole.getRoleId());

            Map<Long, Double> skillScoreMap = new HashMap<>();
            if (latestAttempt.isPresent()) {
                List<QuizResult> results = quizResultRepository.findByAttempt_AttemptId(latestAttempt.get().getAttemptId());
                for (QuizResult qr : results) {
                    double pct = ScoringUtils.calculateScorePercentage(qr.getCorrectCount(), qr.getTotalQuestions());
                    skillScoreMap.put(qr.getSkill().getSkillId(), pct);
                }
            }

            List<SkillGapResponse> gaps = new ArrayList<>();
            for (RoleRequirement req : requirements) {
                Long skillId = req.getSkill().getSkillId();
                String skillName = req.getSkill().getSkillName();
                int reqLevel = req.getRequiredLevel();
                int currentLevel = studentSkillProficiencyMap.getOrDefault(skillId, 1); // Default level 1 if unassessed
                int gap = SkillGapCalculator.calculateGap(reqLevel, currentLevel);
                Double score = skillScoreMap.getOrDefault(skillId, currentLevel * 20.0);

                gaps.add(SkillGapResponse.builder()
                        .skillId(skillId)
                        .skill(skillName)
                        .skillName(skillName)
                        .requiredLevel(reqLevel)
                        .currentLevel(currentLevel)
                        .score(score)
                        .gap(gap)
                        .weakTopics(List.of())
                        .priority(1)
                        .build());
            }

            // Prioritize deterministically: largest gap first, then lower score, then weak topics
            return SkillGapCalculator.prioritizeGaps(gaps);
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("SkillGapService: Error in getSkillGaps: " + ex.getMessage());
            logger.error("Failed to compute skill gaps for student ID: {}", studentId, ex);
            throw new RuntimeException("Error computing skill gaps", ex);
        }
    }

    /**
     * Resolves the target role for a student based on recent attempts or careerGoal string.
     *
     * @param student the student entity
     * @return Role entity or null
     */
    public Role resolveTargetRole(Student student) {
        try {
            Optional<QuizAttempt> latestAttempt = quizAttemptRepository
                    .findFirstByStudent_StudentIdOrderByAttemptDateDesc(student.getStudentId());

            if (latestAttempt.isPresent()) {
                return latestAttempt.get().getRole();
            }

            if (student.getCareerGoal() != null && !student.getCareerGoal().isBlank()) {
                Optional<Role> roleByName = roleRepository.findByRoleName(student.getCareerGoal().trim());
                if (roleByName.isPresent()) {
                    return roleByName.get();
                }
            }

            // Default fallback to first available role (e.g. Data Analyst)
            return roleRepository.findAll().stream().findFirst().orElse(null);
        } catch (Exception ex) {
            System.err.println("SkillGapService: Error resolving target role: " + ex.getMessage());
            logger.error("Failed to resolve target role for student: {}", student.getStudentId(), ex);
            return null;
        }
    }
}
