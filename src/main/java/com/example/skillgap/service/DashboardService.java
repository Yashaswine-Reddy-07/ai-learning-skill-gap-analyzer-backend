package com.example.skillgap.service;

import com.example.skillgap.dto.response.*;
import com.example.skillgap.entity.*;
import com.example.skillgap.exception.ResourceNotFoundException;
import com.example.skillgap.repository.*;
import com.example.skillgap.util.ScoringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service aggregating core dashboard metrics, latest attempt scores, and next recommendations.
 */
@Service
public class DashboardService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

    private final StudentRepository studentRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final SkillGapService skillGapService;
    private final LearningPlanService learningPlanService;
    private final LearningPlanRepository learningPlanRepository;

    public DashboardService(StudentRepository studentRepository,
                            QuizAttemptRepository quizAttemptRepository,
                            SkillGapService skillGapService,
                            LearningPlanService learningPlanService,
                            LearningPlanRepository learningPlanRepository) {
        this.studentRepository = studentRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.skillGapService = skillGapService;
        this.learningPlanService = learningPlanService;
        this.learningPlanRepository = learningPlanRepository;
    }

    /**
     * Aggregates complete dashboard telemetry for a given student.
     *
     * @param studentId the student primary key
     * @return DashboardResponse DTO
     */
    @Transactional(readOnly = true)
    public DashboardResponse getDashboard(Long studentId) {
        try {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> {
                        System.err.println("DashboardService: Student not found with ID: " + studentId);
                        return new ResourceNotFoundException("Student not found with ID: " + studentId);
                    });

            Role role = skillGapService.resolveTargetRole(student);
            String roleName = role != null ? role.getRoleName() : "Target Role";
            Long roleId = role != null ? role.getRoleId() : 1L;

            // Latest attempt
            Optional<QuizAttempt> latestAttempt = quizAttemptRepository
                    .findFirstByStudent_StudentIdOrderByAttemptDateDesc(studentId);

            Double latestScore = latestAttempt.map(QuizAttempt::getTotalScore).orElse(0.0);
            Integer overallLevel = latestAttempt.map(a -> ScoringUtils.convertScoreToProficiencyLevel(a.getTotalScore())).orElse(1);

            // Skill gaps
            List<SkillGapResponse> skillGaps = skillGapService.getSkillGaps(studentId);

            // Learning plan
            List<LearningPlan> planEntities = learningPlanRepository.findByStudent_StudentIdOrderByPriorityAsc(studentId);
            List<LearningPlanItemResponse> progressItems = planEntities.stream()
                    .map(learningPlanService::mapToItemResponse)
                    .toList();

            // Next actions
            List<String> recommendedNext = new ArrayList<>();
            if (skillGaps.isEmpty()) {
                recommendedNext.add("Select a target career role to begin your skills assessment.");
                recommendedNext.add("Take your initial diagnostic skill quiz.");
            } else {
                for (int i = 0; i < Math.min(2, skillGaps.size()); i++) {
                    SkillGapResponse gap = skillGaps.get(i);
                    if (gap.getGap() > 0) {
                        recommendedNext.add("Practice " + gap.getSkillName() + " (Gap: " + gap.getGap() + " levels).");
                    }
                }
                recommendedNext.add("Complete recommended mini-projects in your learning plan.");
                recommendedNext.add("Retake the quiz to verify improvement.");
            }

            int completedCount = (int) planEntities.stream()
                    .filter(p -> "COMPLETED".equalsIgnoreCase(p.getStatus()))
                    .count();
            int totalCount = planEntities.size();
            double overallProgress = totalCount > 0
                    ? Math.round(((double) completedCount / totalCount) * 100.0)
                    : 0.0;

            StudentResponse studentDto = StudentResponse.builder()
                    .studentId(student.getStudentId())
                    .name(student.getName())
                    .email(student.getEmail())
                    .careerGoal(student.getCareerGoal())
                    .build();

            return DashboardResponse.builder()
                    .student(studentDto)
                    .targetRole(roleName)
                    .targetRoleId(roleId)
                    .latestQuizScore(latestScore)
                    .overallLevel(overallLevel)
                    .skillGaps(skillGaps)
                    .recommendedNext(recommendedNext)
                    .learningProgress(progressItems)
                    .completedActivitiesCount(completedCount)
                    .totalActivitiesCount(totalCount)
                    .overallProgressPercentage(overallProgress)
                    .build();
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("DashboardService: Error in getDashboard: " + ex.getMessage());
            logger.error("Failed to build dashboard response for student ID: {}", studentId, ex);
            throw new RuntimeException("Error building dashboard telemetry", ex);
        }
    }
}
