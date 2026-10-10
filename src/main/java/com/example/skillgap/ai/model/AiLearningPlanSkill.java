package com.example.skillgap.ai.model;

import java.util.List;

/**
 * Structured model representation of AI recommendations for a specific skill gap.
 */
public class AiLearningPlanSkill {

    private String skill;
    private Integer priority;
    private Integer currentLevel;
    private Integer requiredLevel;
    private List<String> topics;
    private List<String> learningSteps;
    private List<String> practice;
    private String miniProject;
    private String revision;

    public AiLearningPlanSkill() {
    }

    public AiLearningPlanSkill(String skill, Integer priority, Integer currentLevel, Integer requiredLevel,
                               List<String> topics, List<String> learningSteps, List<String> practice,
                               String miniProject, String revision) {
        this.skill = skill;
        this.priority = priority;
        this.currentLevel = currentLevel;
        this.requiredLevel = requiredLevel;
        this.topics = topics;
        this.learningSteps = learningSteps;
        this.practice = practice;
        this.miniProject = miniProject;
        this.revision = revision;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Integer getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(Integer requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public List<String> getLearningSteps() {
        return learningSteps;
    }

    public void setLearningSteps(List<String> learningSteps) {
        this.learningSteps = learningSteps;
    }

    public List<String> getPractice() {
        return practice;
    }

    public void setPractice(List<String> practice) {
        this.practice = practice;
    }

    public String getMiniProject() {
        return miniProject;
    }

    public void setMiniProject(String miniProject) {
        this.miniProject = miniProject;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public static class Builder {
        private String skill;
        private Integer priority;
        private Integer currentLevel;
        private Integer requiredLevel;
        private List<String> topics;
        private List<String> learningSteps;
        private List<String> practice;
        private String miniProject;
        private String revision;

        public Builder skill(String skill) {
            this.skill = skill;
            return this;
        }

        public Builder priority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public Builder currentLevel(Integer currentLevel) {
            this.currentLevel = currentLevel;
            return this;
        }

        public Builder requiredLevel(Integer requiredLevel) {
            this.requiredLevel = requiredLevel;
            return this;
        }

        public Builder topics(List<String> topics) {
            this.topics = topics;
            return this;
        }

        public Builder learningSteps(List<String> learningSteps) {
            this.learningSteps = learningSteps;
            return this;
        }

        public Builder practice(List<String> practice) {
            this.practice = practice;
            return this;
        }

        public Builder miniProject(String miniProject) {
            this.miniProject = miniProject;
            return this;
        }

        public Builder revision(String revision) {
            this.revision = revision;
            return this;
        }

        public AiLearningPlanSkill build() {
            return new AiLearningPlanSkill(skill, priority, currentLevel, requiredLevel, topics, learningSteps, practice, miniProject, revision);
        }
    }
}
