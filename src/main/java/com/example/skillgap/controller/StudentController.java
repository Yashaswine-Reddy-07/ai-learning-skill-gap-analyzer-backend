package com.example.skillgap.controller;

import com.example.skillgap.dto.response.ApiResponse;
import com.example.skillgap.dto.response.StudentResponse;
import com.example.skillgap.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller exposing student profile management endpoints.
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Retrieves student profile details by student ID.
     *
     * @param id the student primary key
     * @return ApiResponse containing the student profile
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(@PathVariable Long id) {
        try {
            logger.info("Fetching profile for student ID: {}", id);
            StudentResponse response = studentService.getStudentById(id);
            return ResponseEntity.ok(ApiResponse.success("Student profile retrieved", response));
        } catch (Exception ex) {
            System.err.println("StudentController: Error in getStudentById: " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Updates career goal on the student's profile.
     *
     * @param id      the student ID
     * @param payload map containing "careerGoal" string
     * @return ApiResponse containing updated student profile
     */
    @PutMapping("/{id}/goal")
    public ResponseEntity<ApiResponse<StudentResponse>> updateCareerGoal(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        try {
            String careerGoal = payload.get("careerGoal");
            logger.info("Updating career goal for student ID: {} to {}", id, careerGoal);
            StudentResponse response = studentService.updateCareerGoal(id, careerGoal);
            return ResponseEntity.ok(ApiResponse.success("Career goal updated successfully", response));
        } catch (Exception ex) {
            System.err.println("StudentController: Error in updateCareerGoal: " + ex.getMessage());
            throw ex;
        }
    }
}
