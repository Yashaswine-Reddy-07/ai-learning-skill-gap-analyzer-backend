package com.example.skillgap.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standardized error response envelope returned on application exceptions.
 */
public class ApiErrorResponse {

    private boolean success = false;
    private String message;
    private String errorCode;
    private List<String> details;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(boolean success, String message, String errorCode, List<String> details, LocalDateTime timestamp) {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
        this.details = details;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ApiErrorResponse of(String message, String errorCode) {
        return new ApiErrorResponse(false, message, errorCode, null, LocalDateTime.now());
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static class Builder {
        private boolean success = false;
        private String message;
        private String errorCode;
        private List<String> details;
        private LocalDateTime timestamp = LocalDateTime.now();

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder details(List<String> details) {
            this.details = details;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ApiErrorResponse build() {
            return new ApiErrorResponse(success, message, errorCode, details, timestamp);
        }
    }
}
