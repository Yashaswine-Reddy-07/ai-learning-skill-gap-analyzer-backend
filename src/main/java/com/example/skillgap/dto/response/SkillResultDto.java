package com.example.skillgap.dto.response;

import java.util.List;

/**
 * Detailed evaluation outcome for a specific skill within a quiz attempt.
 */
public class SkillResultDto {

    private Long skillId;
    private String skill;
    private String skillName;
    private Double score;
    private Integer level;
    private Integer requiredLevel;
    private Integer gap;
    private List<String> wrongTopics;
    private Integer priority;

    public SkillResultDto() {
    }

    public SkillResultDto(Long skillId, String skill, String skillName, Double score, Integer level,
                          Integer requiredLevel, Integer gap, List<String> wrongTopics, Integer priority) {
        this.skillId = skillId;
        this.skill = skill;
        this.skillName = skillName;
        this.score = score;
        this.level = level;
        this.requiredLevel = requiredLevel;
        this.gap = gap;
        this.wrongTopics = wrongTopics;
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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(Integer requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public Integer getGap() {
        return gap;
    }

    public void setGap(Integer gap) {
        this.gap = gap;
    }

    public List<String> getWrongTopics() {
        return wrongTopics;
    }

    public void setWrongTopics(List<String> wrongTopics) {
        this.wrongTopics = wrongTopics;
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
        private Double score;
        private Integer level;
        private Integer requiredLevel;
        private Integer gap;
        private List<String> wrongTopics;
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

        public Builder score(Double score) {
            this.score = score;
            return this;
        }

        public Builder level(Integer level) {
            this.level = level;
            return this;
        }

        public Builder requiredLevel(Integer requiredLevel) {
            this.requiredLevel = requiredLevel;
            return this;
        }

        public Builder gap(Integer gap) {
            this.gap = gap;
            return this;
        }

        public Builder wrongTopics(List<String> wrongTopics) {
            this.wrongTopics = wrongTopics;
            return this;
        }

        public Builder priority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public SkillResultDto build() {
            return new SkillResultDto(skillId, skill, skillName, score, level, requiredLevel, gap, wrongTopics, priority);
        }
    }
}
