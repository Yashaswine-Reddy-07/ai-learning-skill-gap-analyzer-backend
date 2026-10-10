package com.example.skillgap.dto.response;

import java.util.List;

/**
 * Representation of a student's evaluated skill gap against their selected career benchmark.
 */
public class SkillGapResponse {

    private Long skillId;
    private String skill;
    private String skillName;
    private Integer requiredLevel;
    private Integer currentLevel;
    private Double score;
    private Integer gap;
    private List<String> weakTopics;
    private Integer priority;

    public SkillGapResponse() {
    }

    public SkillGapResponse(Long skillId, String skill, String skillName, Integer requiredLevel,
                            Integer currentLevel, Double score, Integer gap, List<String> weakTopics, Integer priority) {
        this.skillId = skillId;
        this.skill = skill;
        this.skillName = skillName;
        this.requiredLevel = requiredLevel;
        this.currentLevel = currentLevel;
        this.score = score;
        this.gap = gap;
        this.weakTopics = weakTopics;
        this.priority = priority;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Integer getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(Integer requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getGap() {
        return gap;
    }

    public void setGap(Integer gap) {
        this.gap = gap;
    }

    public List<String> getWeakTopics() {
        return weakTopics;
    }

    public void setWeakTopics(List<String> weakTopics) {
        this.weakTopics = weakTopics;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public static class Builder {
        private Long skillId;
        private String skill;
        private String skillName;
        private Integer requiredLevel;
        private Integer currentLevel;
        private Double score;
        private Integer gap;
        private List<String> weakTopics;
        private Integer priority;

        public Builder skillId(Long skillId) {
            this.skillId = skillId;
            return this;
        }

        public Builder skill(String skill) {
            this.skill = skill;
            return this;
        }

        public Builder skillName(String skillName) {
            this.skillName = skillName;
            return this;
        }

        public Builder requiredLevel(Integer requiredLevel) {
            this.requiredLevel = requiredLevel;
            return this;
        }

        public Builder currentLevel(Integer currentLevel) {
            this.currentLevel = currentLevel;
            return this;
        }

        public Builder score(Double score) {
            this.score = score;
            return this;
        }

        public Builder gap(Integer gap) {
            this.gap = gap;
            return this;
        }

        public Builder weakTopics(List<String> weakTopics) {
            this.weakTopics = weakTopics;
            return this;
        }

        public Builder priority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public SkillGapResponse build() {
            return new SkillGapResponse(skillId, skill, skillName, requiredLevel, currentLevel, score, gap, weakTopics, priority);
        }
    }
}
