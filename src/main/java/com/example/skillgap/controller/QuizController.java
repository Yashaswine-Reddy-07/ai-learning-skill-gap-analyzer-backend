package com.example.skillgap.controller;

import com.example.skillgap.dto.request.QuizSubmitRequest;
import com.example.skillgap.dto.response.ApiResponse;
import com.example.skillgap.dto.response.QuizResultResponse;
import com.example.skillgap.service.QuizService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller exposing quiz answer submission and evaluation results lookup.
 */
@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    /**
     * Submits student answers for backend evaluation, score calculation, and skill-gap detection.
     *
     * @param request the validated quiz submission request
     * @return ApiResponse containing comprehensive QuizResultResponse
     */
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<QuizResultResponse>> submitQuiz(@Valid @RequestBody QuizSubmitRequest request) {
        try {
            logger.info("Processing quiz submission for student ID: {} and role ID: {}",
                    request.getStudentId(), request.getRoleId());
            QuizResultResponse response = quizService.submitQuiz(request);
            return ResponseEntity.ok(ApiResponse.success("Quiz submitted and evaluated successfully", response));
        } catch (Exception ex) {
            System.err.println("QuizController: Error in submitQuiz: " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Retrieves objective scoring outcomes for a previously completed quiz attempt.
     *
     * @param attemptId the attempt primary key
     * @return ApiResponse containing QuizResultResponse
     */
    @GetMapping("/results/{attemptId}")
    public ResponseEntity<ApiResponse<QuizResultResponse>> getQuizResult(@PathVariable Long attemptId) {
        try {
            logger.info("Fetching quiz result for attempt ID: {}", attemptId);
            QuizResultResponse response = quizService.getQuizResult(attemptId);
            return ResponseEntity.ok(ApiResponse.success("Quiz results retrieved successfully", response));
        } catch (Exception ex) {
            System.err.println("QuizController: Error in getQuizResult: " + ex.getMessage());
            throw ex;
        }
    }
}
