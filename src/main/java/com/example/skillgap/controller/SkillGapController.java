package com.example.skillgap.controller;

import com.example.skillgap.dto.response.ApiResponse;
import com.example.skillgap.dto.response.SkillGapResponse;
import com.example.skillgap.service.SkillGapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller exposing skill gap analysis endpoints.
 */
@RestController
@RequestMapping("/api/skills")
public class SkillGapController {

    private static final Logger logger = LoggerFactory.getLogger(SkillGapController.class);

    private final SkillGapService skillGapService;

    public SkillGapController(SkillGapService skillGapService) {
        this.skillGapService = skillGapService;
    }

    /**
     * Retrieves prioritized skill gaps comparing the student's target role requirements
     * against their actual assessed proficiency.
     *
     * @param studentId the student primary key
     * @return ApiResponse containing list of prioritized SkillGapResponse items
     */
    @GetMapping("/gaps/{studentId}")
    public ResponseEntity<ApiResponse<List<SkillGapResponse>>> getSkillGaps(@PathVariable Long studentId) {
        try {
            logger.info("Computing skill gaps for student ID: {}", studentId);
            List<SkillGapResponse> gaps = skillGapService.getSkillGaps(studentId);
            return ResponseEntity.ok(ApiResponse.success("Skill gaps retrieved successfully", gaps));
        } catch (Exception ex) {
            System.err.println("SkillGapController: Error in getSkillGaps: " + ex.getMessage());
            throw ex;
        }
    }
}
