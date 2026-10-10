package com.example.skillgap.controller;

import com.example.skillgap.dto.response.ApiResponse;
import com.example.skillgap.dto.response.DashboardResponse;
import com.example.skillgap.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller exposing aggregated dashboard telemetry for student profiles.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Retrieves aggregated dashboard telemetry (target role, quiz score, gaps, progress) for a student.
     *
     * @param studentId the student primary key
     * @return ApiResponse containing DashboardResponse DTO
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(@PathVariable Long studentId) {
        try {
            logger.info("Fetching dashboard telemetry for student ID: {}", studentId);
            DashboardResponse response = dashboardService.getDashboard(studentId);
            return ResponseEntity.ok(ApiResponse.success("Dashboard telemetry retrieved successfully", response));
        } catch (Exception ex) {
            System.err.println("DashboardController: Error in getDashboard: " + ex.getMessage());
            throw ex;
        }
    }
}
