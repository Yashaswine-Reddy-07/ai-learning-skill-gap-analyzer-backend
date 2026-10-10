package com.example.skillgap.controller;

import com.example.skillgap.dto.response.ApiResponse;
import com.example.skillgap.dto.response.LearningPlanResponse;
import com.example.skillgap.service.LearningPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller exposing personalized learning plan queries.
 */
@RestController
@RequestMapping("/api/learning-plan")
public class LearningPlanController {

    private static final Logger logger = LoggerFactory.getLogger(LearningPlanController.class);

    private final LearningPlanService learningPlanService;

    public LearningPlanController(LearningPlanService learningPlanService) {
        this.learningPlanService = learningPlanService;
    }

    /**
     * Retrieves the active personalized learning plan items for a student.
     *
     * @param studentId the student primary key
     * @return ApiResponse containing LearningPlanResponse DTO
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<ApiResponse<LearningPlanResponse>> getLearningPlan(@PathVariable Long studentId) {
        try {
            logger.info("Fetching personalized learning plan for student ID: {}", studentId);
            LearningPlanResponse response = learningPlanService.getLearningPlan(studentId);
            return ResponseEntity.ok(ApiResponse.success("Learning plan retrieved successfully", response));
        } catch (Exception ex) {
            System.err.println("LearningPlanController: Error in getLearningPlan: " + ex.getMessage());
            throw ex;
        }
    }
}
