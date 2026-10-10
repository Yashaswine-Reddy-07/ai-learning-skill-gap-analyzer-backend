package com.example.skillgap.controller;

import com.example.skillgap.dto.response.ApiResponse;
import com.example.skillgap.dto.response.QuizResponse;
import com.example.skillgap.dto.response.RoleResponse;
import com.example.skillgap.service.QuizService;
import com.example.skillgap.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller exposing career roles and quiz generation endpoints.
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    private final RoleService roleService;
    private final QuizService quizService;

    public RoleController(RoleService roleService, QuizService quizService) {
        this.roleService = roleService;
        this.quizService = quizService;
    }

    /**
     * Retrieves all configured career roles and their required skill benchmarks.
     *
     * @return ApiResponse containing list of RoleResponse DTOs
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        try {
            logger.info("Fetching all career roles");
            List<RoleResponse> roles = roleService.getAllRoles();
            return ResponseEntity.ok(ApiResponse.success("Career roles retrieved successfully", roles));
        } catch (Exception ex) {
            System.err.println("RoleController: Error in getAllRoles: " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Retrieves a single career role and its skill requirements.
     *
     * @param id the role primary key
     * @return ApiResponse containing RoleResponse DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleById(@PathVariable Long id) {
        try {
            logger.info("Fetching career role details for ID: {}", id);
            RoleResponse role = roleService.getRoleById(id);
            return ResponseEntity.ok(ApiResponse.success("Role details retrieved", role));
        } catch (Exception ex) {
            System.err.println("RoleController: Error in getRoleById: " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Generates a role-based objective quiz with ~5 questions per required skill.
     * STRICT SECURITY RULE: Correct answers are never sent to the client.
     *
     * @param id the role primary key
     * @return ApiResponse containing sanitized QuizResponse
     */
    @GetMapping("/{id}/quiz")
    public ResponseEntity<ApiResponse<QuizResponse>> generateQuizForRole(@PathVariable Long id) {
        try {
            logger.info("Generating objective skill quiz for role ID: {}", id);
            QuizResponse quiz = quizService.generateQuiz(id);
            return ResponseEntity.ok(ApiResponse.success("Quiz generated successfully", quiz));
        } catch (Exception ex) {
            System.err.println("RoleController: Error in generateQuizForRole: " + ex.getMessage());
            throw ex;
        }
    }
}
