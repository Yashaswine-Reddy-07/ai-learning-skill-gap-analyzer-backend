package com.example.skillgap.service;

import com.example.skillgap.ai.model.AiLearningPlanJson;
import com.example.skillgap.ai.model.AiLearningPlanSkill;
import com.example.skillgap.ai.service.LangChain4jRecommendationService;
import com.example.skillgap.dto.request.ProgressUpdateRequest;
import com.example.skillgap.dto.request.RecommendationRequest;
import com.example.skillgap.dto.response.LearningPlanItemResponse;
import com.example.skillgap.dto.response.LearningPlanResponse;
import com.example.skillgap.dto.response.SkillGapResponse;
import com.example.skillgap.entity.LearningPlan;
import com.example.skillgap.entity.Role;
import com.example.skillgap.entity.Student;
import com.example.skillgap.exception.BadRequestException;
import com.example.skillgap.exception.ResourceNotFoundException;
import com.example.skillgap.repository.LearningPlanRepository;
import com.example.skillgap.repository.RoleRepository;
import com.example.skillgap.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service orchestrating AI learning plan synthesis, database persistence, and student progress tracking.
 */
@Service
public class LearningPlanService {

    private static final Logger logger = LoggerFactory.getLogger(LearningPlanService.class);

    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final LearningPlanRepository learningPlanRepository;
    private final SkillGapService skillGapService;
    private final LangChain4jRecommendationService recommendationService;

    public LearningPlanService(StudentRepository studentRepository,
                               RoleRepository roleRepository,
                               LearningPlanRepository learningPlanRepository,
                               SkillGapService skillGapService,
                               LangChain4jRecommendationService recommendationService) {
        this.studentRepository = studentRepository;
        this.roleRepository = roleRepository;
        this.learningPlanRepository = learningPlanRepository;
        this.skillGapService = skillGapService;
        this.recommendationService = recommendationService;
    }

    /**
     * Generates a personalized learning curriculum using LangChain4j structured context
     * and persists actionable milestones into the learning_plans table.
     *
     * @param request the recommendation trigger request DTO
     * @return populated LearningPlanResponse DTO
     */
    @Transactional
    public LearningPlanResponse generateAndSaveLearningPlan(RecommendationRequest request) {
        try {
            Student student = studentRepository.findById(request.getStudentId())
                    .orElseThrow(() -> {
                        System.err.println("LearningPlanService: Student not found with ID: " + request.getStudentId());
                        return new ResourceNotFoundException("Student not found with ID: " + request.getStudentId());
                    });

            Role role;
            if (request.getRoleId() != null) {
                role = roleRepository.findById(request.getRoleId())
                        .orElse(skillGapService.resolveTargetRole(student));
            } else {
                role = skillGapService.resolveTargetRole(student);
            }

            String roleName = role != null ? role.getRoleName() : "Technical Role";

            // Compute objective skill gaps
            List<SkillGapResponse> gaps = skillGapService.getSkillGaps(student.getStudentId());
            if (gaps.isEmpty()) {
                System.err.println("LearningPlanService: No skill gaps evaluated for student ID: " + student.getStudentId());
                throw new BadRequestException("Please take a career quiz first to evaluate your objective skill gaps.");
            }

            // Call LangChain4j AI service
            AiLearningPlanJson aiPlan = recommendationService.generatePlan(roleName, gaps);

            // Remove previous learning plan items for this student
            learningPlanRepository.deleteByStudent_StudentId(student.getStudentId());

            List<LearningPlan> entitiesToSave = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();

            for (AiLearningPlanSkill s : aiPlan.getSkills()) {
                String skillName = s.getSkill();
                int priority = s.getPriority() != null ? s.getPriority() : 1;

                // 1. Learning Steps Activity
                if (s.getLearningSteps() != null && !s.getLearningSteps().isEmpty()) {
                    String stepsText = "Study Steps: " + String.join(" | ", s.getLearningSteps());
                    entitiesToSave.add(LearningPlan.builder()
                            .student(student)
                            .skill(skillName)
                            .topic(s.getTopics() != null && !s.getTopics().isEmpty() ? s.getTopics().get(0) : "Theory & Concepts")
                            .activity(stepsText)
                            .priority(priority)
                            .progress(0)
                            .status("NOT_STARTED")
                            .createdDate(now)
                            .build());
                }

                // 2. Practice Lab Activity
                if (s.getPractice() != null && !s.getPractice().isEmpty()) {
                    String practiceText = "Hands-on Practice: " + String.join(" | ", s.getPractice());
                    entitiesToSave.add(LearningPlan.builder()
                            .student(student)
                            .skill(skillName)
                            .topic("Practical Labs")
                            .activity(practiceText)
                            .priority(priority)
                            .progress(0)
                            .status("NOT_STARTED")
                            .createdDate(now)
                            .build());
                }

                // 3. Mini Project Activity
                if (s.getMiniProject() != null && !s.getMiniProject().isBlank()) {
                    entitiesToSave.add(LearningPlan.builder()
                            .student(student)
                            .skill(skillName)
                            .topic("Portfolio Project")
                            .activity("Project Task: " + s.getMiniProject())
                            .priority(priority)
                            .progress(0)
                            .status("NOT_STARTED")
                            .createdDate(now)
                            .build());
                }

                // 4. Revision Checklist
                if (s.getRevision() != null && !s.getRevision().isBlank()) {
                    entitiesToSave.add(LearningPlan.builder()
                            .student(student)
                            .skill(skillName)
                            .topic("Revision & Review")
                            .activity("Self-Audit: " + s.getRevision())
                            .priority(priority)
                            .progress(0)
                            .status("NOT_STARTED")
                            .createdDate(now)
                            .build());
                }
            }

            List<LearningPlan> saved = learningPlanRepository.saveAll(entitiesToSave);
            List<LearningPlanItemResponse> itemResponses = saved.stream()
                    .map(this::mapToItemResponse)
                    .toList();

            return LearningPlanResponse.builder()
                    .studentId(student.getStudentId())
                    .roleName(roleName)
                    .summary(aiPlan.getSummary())
                    .retakeRecommendation(aiPlan.getRetakeRecommendation())
                    .items(itemResponses)
                    .build();
        } catch (ResourceNotFoundException | BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("LearningPlanService: Error generating plan: " + ex.getMessage());
            logger.error("Failed to generate and save learning plan", ex);
            throw new RuntimeException("Error generating learning plan", ex);
        }
    }

    /**
     * Retrieves all saved learning plan items for a student.
     *
     * @param studentId the student primary key
     * @return LearningPlanResponse containing curriculum milestones
     */
    @Transactional(readOnly = true)
    public LearningPlanResponse getLearningPlan(Long studentId) {
        try {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> {
                        System.err.println("LearningPlanService: Student not found with ID: " + studentId);
                        return new ResourceNotFoundException("Student not found with ID: " + studentId);
                    });

            Role role = skillGapService.resolveTargetRole(student);
            String roleName = role != null ? role.getRoleName() : "Technical Role";

            List<LearningPlan> items = learningPlanRepository.findByStudent_StudentIdOrderByPriorityAsc(studentId);
            List<LearningPlanItemResponse> itemResponses = items.stream()
                    .map(this::mapToItemResponse)
                    .toList();

            String summary = items.isEmpty()
                    ? "No personalized learning plan has been generated yet. Please take a quiz and request recommendations."
                    : "Current active personalized learning roadmap for " + roleName + ".";

            String retake = "Retake the assessment after completing the priority 1 and priority 2 milestones.";

            return LearningPlanResponse.builder()
                    .studentId(studentId)
                    .roleName(roleName)
                    .summary(summary)
                    .retakeRecommendation(retake)
                    .items(itemResponses)
                    .build();
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("LearningPlanService: Error in getLearningPlan: " + ex.getMessage());
            logger.error("Failed to fetch learning plan for student ID: {}", studentId, ex);
            throw new RuntimeException("Error fetching learning plan", ex);
        }
    }

    /**
     * Updates completion progress and status for an individual learning plan milestone.
     *
     * @param planId  the learning plan primary key
     * @param request the progress update request
     * @return updated LearningPlanItemResponse DTO
     */
    @Transactional
    public LearningPlanItemResponse updateProgress(Long planId, ProgressUpdateRequest request) {
        try {
            LearningPlan plan = learningPlanRepository.findById(planId)
                    .orElseThrow(() -> {
                        System.err.println("LearningPlanService: Plan item not found with ID: " + planId);
                        return new ResourceNotFoundException("Learning plan item not found with ID: " + planId);
                    });

            plan.setProgress(request.getProgress());

            String status = request.getStatus().trim().toUpperCase();
            if (request.getProgress() >= 100) {
                status = "COMPLETED";
            } else if (request.getProgress() > 0 && "NOT_STARTED".equals(status)) {
                status = "IN_PROGRESS";
            }
            plan.setStatus(status);

            LearningPlan saved = learningPlanRepository.save(plan);
            return mapToItemResponse(saved);
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("LearningPlanService: Error in updateProgress: " + ex.getMessage());
            logger.error("Failed to update progress for plan ID: {}", planId, ex);
            throw new RuntimeException("Error updating learning progress", ex);
        }
    }

    /**
     * Maps a LearningPlan entity to a safe LearningPlanItemResponse DTO.
     *
     * @param item the LearningPlan entity
     * @return LearningPlanItemResponse DTO
     */
    public LearningPlanItemResponse mapToItemResponse(LearningPlan item) {
        try {
            return LearningPlanItemResponse.builder()
                    .planId(item.getPlanId())
                    .studentId(item.getStudent().getStudentId())
                    .skill(item.getSkill())
                    .topic(item.getTopic())
                    .activity(item.getActivity())
                    .priority(item.getPriority())
                    .progress(item.getProgress())
                    .status(item.getStatus())
                    .createdDate(item.getCreatedDate())
                    .build();
        } catch (Exception ex) {
            System.err.println("LearningPlanService: Error mapping item response: " + ex.getMessage());
            return LearningPlanItemResponse.builder()
                    .planId(item.getPlanId())
                    .skill(item.getSkill())
                    .activity(item.getActivity())
                    .progress(0)
                    .status("NOT_STARTED")
                    .build();
        }
    }
}
