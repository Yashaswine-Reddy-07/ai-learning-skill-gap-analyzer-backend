package com.example.skillgap.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * Request payload to initiate LangChain4j personalized learning plan generation.
 */
public class RecommendationRequest {

    @NotNull(message = "Student ID cannot be null")
    private Long studentId;

    private Long roleId;

    public RecommendationRequest() {
    }

    public RecommendationRequest(Long studentId, Long roleId) {
        this.studentId = studentId;
        this.roleId = roleId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public static class Builder {
        private Long studentId;
        private Long roleId;

        public Builder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder roleId(Long roleId) {
            this.roleId = roleId;
            return this;
        }

        public RecommendationRequest build() {
            return new RecommendationRequest(studentId, roleId);
        }
    }
}
