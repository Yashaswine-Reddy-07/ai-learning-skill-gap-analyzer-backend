package com.example.skillgap.exception;

import com.example.skillgap.dto.response.ApiErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * Centralized Global Exception Handler intercepting all controller exceptions.
 * Produces structured ApiErrorResponse envelopes and sanitizes stack traces.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles ResourceNotFoundException and returns HTTP 404 NOT_FOUND.
     *
     * @param ex the ResourceNotFoundException
     * @return standardized ApiErrorResponse
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        try {
            System.err.println("GlobalExceptionHandler [404 NOT FOUND]: " + ex.getMessage());
            logger.warn("Resource not found: {}", ex.getMessage());
            ApiErrorResponse response = ApiErrorResponse.of(ex.getMessage(), "RESOURCE_NOT_FOUND");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception handlerEx) {
            System.err.println("GlobalExceptionHandler: Fallback error in handleResourceNotFoundException: " + handlerEx.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse.of("An unexpected error occurred", "INTERNAL_ERROR"));
        }
    }

    /**
     * Handles BadRequestException and returns HTTP 400 BAD_REQUEST.
     *
     * @param ex the BadRequestException
     * @return standardized ApiErrorResponse
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException ex) {
        try {
            System.err.println("GlobalExceptionHandler [400 BAD REQUEST]: " + ex.getMessage());
            logger.warn("Bad request: {}", ex.getMessage());
            ApiErrorResponse response = ApiErrorResponse.of(ex.getMessage(), "BAD_REQUEST");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception handlerEx) {
            System.err.println("GlobalExceptionHandler: Fallback error in handleBadRequestException: " + handlerEx.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse.of("An unexpected error occurred", "INTERNAL_ERROR"));
        }
    }

    /**
     * Handles UnauthorizedException and returns HTTP 401 UNAUTHORIZED.
     *
     * @param ex the UnauthorizedException
     * @return standardized ApiErrorResponse
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        try {
            System.err.println("GlobalExceptionHandler [401 UNAUTHORIZED]: " + ex.getMessage());
            logger.warn("Unauthorized request: {}", ex.getMessage());
            ApiErrorResponse response = ApiErrorResponse.of(ex.getMessage(), "UNAUTHORIZED");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception handlerEx) {
            System.err.println("GlobalExceptionHandler: Fallback error in handleUnauthorizedException: " + handlerEx.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse.of("An unexpected error occurred", "INTERNAL_ERROR"));
        }
    }

    /**
     * Handles BadCredentialsException thrown during Spring Security authentication.
     *
     * @param ex the BadCredentialsException
     * @return standardized ApiErrorResponse
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        try {
            System.err.println("GlobalExceptionHandler [401 BAD CREDENTIALS]: " + ex.getMessage());
            logger.warn("Bad credentials error: {}", ex.getMessage());
            ApiErrorResponse response = ApiErrorResponse.of("Invalid email or password", "INVALID_CREDENTIALS");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception handlerEx) {
            System.err.println("GlobalExceptionHandler: Fallback error in handleBadCredentialsException: " + handlerEx.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse.of("An unexpected error occurred", "INTERNAL_ERROR"));
        }
    }

    /**
     * Handles DTO validation constraint violations (@Valid) and returns HTTP 400.
     *
     * @param ex the MethodArgumentNotValidException
     * @return standardized ApiErrorResponse with field error list
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        try {
            System.err.println("GlobalExceptionHandler [400 VALIDATION ERROR]: Field validation failed");
            List<String> errors = new ArrayList<>();
            for (FieldError error : ex.getBindingResult().getFieldErrors()) {
                String errorMsg = error.getField() + ": " + error.getDefaultMessage();
                errors.add(errorMsg);
                System.err.println("Validation detail: " + errorMsg);
            }
            ApiErrorResponse response = ApiErrorResponse.builder()
                    .success(false)
                    .message("Validation failed for input data")
                    .errorCode("VALIDATION_FAILED")
                    .details(errors)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception handlerEx) {
            System.err.println("GlobalExceptionHandler: Fallback error in handleValidationException: " + handlerEx.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse.of("Validation parsing failure", "INTERNAL_ERROR"));
        }
    }

    /**
     * Catch-all handler for unhandled exceptions to prevent stack trace leaks.
     *
     * @param ex the general Exception
     * @return standardized ApiErrorResponse with generic message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex) {
        try {
            System.err.println("GlobalExceptionHandler [500 INTERNAL ERROR]: " + ex.getClass().getName() + " - " + ex.getMessage());
            logger.error("Unhandled internal server error occurred", ex);
            ApiErrorResponse response = ApiErrorResponse.of("An unexpected error occurred. Please try again later.", "INTERNAL_SERVER_ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception fallbackEx) {
            System.err.println("Critical error in general exception handler: " + fallbackEx.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse.of("Fatal server error", "FATAL_ERROR"));
        }
    }
}
