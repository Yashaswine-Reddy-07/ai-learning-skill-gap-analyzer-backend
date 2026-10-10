package com.example.skillgap.dto.response;

import java.util.List;

/**
 * Encapsulation of the personalized learning plan for a student.
 */
public class LearningPlanResponse {

    private Long studentId;
    private String roleName;
    private String summary;
    private String retakeRecommendation;
    private List<LearningPlanItemResponse> items;

    public LearningPlanResponse() {
    }

    public LearningPlanResponse(Long studentId, String roleName, String summary,
                                String retakeRecommendation, List<LearningPlanItemResponse> items) {
        this.studentId = studentId;
        this.roleName = roleName;
        this.summary = summary;
        this.retakeRecommendation = retakeRecommendation;
        this.items = items;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRetakeRecommendation() {
        return retakeRecommendation;
    }

    public void setRetakeRecommendation(String retakeRecommendation) {
        this.retakeRecommendation = retakeRecommendation;
    }

    public List<LearningPlanItemResponse> getItems() {
        return items;
    }

    public void setItems(List<LearningPlanItemResponse> items) {
        this.items = items;
    }

    public static class Builder {
        private Long studentId;
        private String roleName;
        private String summary;
        private String retakeRecommendation;
        private List<LearningPlanItemResponse> items;

        public Builder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder roleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder retakeRecommendation(String retakeRecommendation) {
            this.retakeRecommendation = retakeRecommendation;
            return this;
        }

        public Builder items(List<LearningPlanItemResponse> items) {
            this.items = items;
            return this;
        }

        public LearningPlanResponse build() {
            return new LearningPlanResponse(studentId, roleName, summary, retakeRecommendation, items);
        }
    }
}
