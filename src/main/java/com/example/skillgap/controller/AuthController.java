package com.example.skillgap.controller;

import com.example.skillgap.dto.request.LoginRequest;
import com.example.skillgap.dto.request.RegisterRequest;
import com.example.skillgap.dto.response.ApiResponse;
import com.example.skillgap.dto.response.AuthResponse;
import com.example.skillgap.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller exposing authentication endpoints for student account creation and login.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registers a new student account.
     *
     * @param request the validated registration request
     * @return ApiResponse containing JWT and student metadata
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            logger.info("Received registration request for email: {}", request.getEmail());
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Student account registered successfully", response));
        } catch (Exception ex) {
            System.err.println("AuthController: Error during registration: " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Authenticates a student and returns an authorization token.
     *
     * @param request the validated login request
     * @return ApiResponse containing JWT and student metadata
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            logger.info("Received login request for email: {}", request.getEmail());
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Authentication successful", response));
        } catch (Exception ex) {
            System.err.println("AuthController: Error during login: " + ex.getMessage());
            throw ex;
        }
    }
}
