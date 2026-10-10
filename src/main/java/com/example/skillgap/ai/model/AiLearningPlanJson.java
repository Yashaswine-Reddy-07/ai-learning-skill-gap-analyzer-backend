package com.example.skillgap.ai.model;

import java.util.List;

/**
 * Top-level structured JSON contract parsed from LangChain4j response.
 */
public class AiLearningPlanJson {

    private String role;
    private String summary;
    private List<AiLearningPlanSkill> skills;
    private String retakeRecommendation;

    public AiLearningPlanJson() {
    }

    public AiLearningPlanJson(String role, String summary, List<AiLearningPlanSkill> skills, String retakeRecommendation) {
        this.role = role;
        this.summary = summary;
        this.skills = skills;
        this.retakeRecommendation = retakeRecommendation;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<AiLearningPlanSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<AiLearningPlanSkill> skills) {
        this.skills = skills;
    }

    public String getRetakeRecommendation() {
        return retakeRecommendation;
    }

    public void setRetakeRecommendation(String retakeRecommendation) {
        this.retakeRecommendation = retakeRecommendation;
    }

    public static class Builder {
        private String role;
        private String summary;
        private List<AiLearningPlanSkill> skills;
        private String retakeRecommendation;

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder skills(List<AiLearningPlanSkill> skills) {
            this.skills = skills;
            return this;
        }

        public Builder retakeRecommendation(String retakeRecommendation) {
            this.retakeRecommendation = retakeRecommendation;
            return this;
        }

        public AiLearningPlanJson build() {
            return new AiLearningPlanJson(role, summary, skills, retakeRecommendation);
        }
    }
}
