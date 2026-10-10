package com.example.skillgap.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request payload for updating learning activity completion progress and status.
 */
public class ProgressUpdateRequest {

    @NotNull(message = "Progress value cannot be null")
    @Min(value = 0, message = "Progress cannot be less than 0")
    @Max(value = 100, message = "Progress cannot exceed 100")
    private Integer progress;

    @NotBlank(message = "Status cannot be blank")
    private String status; // NOT_STARTED, IN_PROGRESS, COMPLETED

    public ProgressUpdateRequest() {
    }

    public ProgressUpdateRequest(Integer progress, String status) {
        this.progress = progress;
        this.status = status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Builder {
        private Integer progress;
        private String status;

        public Builder progress(Integer progress) {
            this.progress = progress;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public ProgressUpdateRequest build() {
            return new ProgressUpdateRequest(progress, status);
        }
    }
}
