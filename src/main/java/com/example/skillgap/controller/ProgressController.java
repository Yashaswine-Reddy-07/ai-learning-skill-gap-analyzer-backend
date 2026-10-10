package com.example.skillgap.controller;

import com.example.skillgap.dto.request.ProgressUpdateRequest;
import com.example.skillgap.dto.response.ApiResponse;
import com.example.skillgap.dto.response.LearningPlanItemResponse;
import com.example.skillgap.service.LearningPlanService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller exposing milestone progress tracking updates.
 */
@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private static final Logger logger = LoggerFactory.getLogger(ProgressController.class);

    private final LearningPlanService learningPlanService;

    public ProgressController(LearningPlanService learningPlanService) {
        this.learningPlanService = learningPlanService;
    }

    /**
     * Updates completion progress percentage and status for a specific learning plan item.
     *
     * @param id      the learning plan milestone primary key
     * @param request the validated progress update request
     * @return ApiResponse containing the updated LearningPlanItemResponse
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LearningPlanItemResponse>> updateProgress(
            @PathVariable Long id,
            @Valid @RequestBody ProgressUpdateRequest request) {
        try {
            logger.info("Updating progress for plan ID: {} to {}% ({})", id, request.getProgress(), request.getStatus());
            LearningPlanItemResponse response = learningPlanService.updateProgress(id, request);
            return ResponseEntity.ok(ApiResponse.success("Progress updated successfully", response));
        } catch (Exception ex) {
            System.err.println("ProgressController: Error in updateProgress: " + ex.getMessage());
            throw ex;
        }
    }
}
