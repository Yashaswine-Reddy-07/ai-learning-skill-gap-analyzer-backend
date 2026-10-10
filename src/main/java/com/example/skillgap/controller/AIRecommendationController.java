package com.example.skillgap.controller;

import com.example.skillgap.dto.request.RecommendationRequest;
import com.example.skillgap.dto.response.ApiResponse;
import com.example.skillgap.dto.response.LearningPlanResponse;
import com.example.skillgap.service.LearningPlanService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller exposing LangChain4j personalized AI recommendation generation.
 */
@RestController
@RequestMapping("/api/ai")
public class AIRecommendationController {

    private static final Logger logger = LoggerFactory.getLogger(AIRecommendationController.class);

    private final LearningPlanService learningPlanService;

    public AIRecommendationController(LearningPlanService learningPlanService) {
        this.learningPlanService = learningPlanService;
    }

    /**
     * Synthesizes and saves a personalized learning plan using LangChain4j structured context.
     *
     * @param request the validated recommendation request DTO
     * @return ApiResponse containing the generated LearningPlanResponse
     */
    @PostMapping("/recommendations")
    public ResponseEntity<ApiResponse<LearningPlanResponse>> generateRecommendations(
            @Valid @RequestBody RecommendationRequest request) {
        try {
            logger.info("Generating personalized AI recommendations for student ID: {}", request.getStudentId());
            LearningPlanResponse response = learningPlanService.generateAndSaveLearningPlan(request);
            return ResponseEntity.ok(ApiResponse.success("AI learning plan generated and saved successfully", response));
        } catch (Exception ex) {
            System.err.println("AIRecommendationController: Error in generateRecommendations: " + ex.getMessage());
            throw ex;
        }
    }
}
