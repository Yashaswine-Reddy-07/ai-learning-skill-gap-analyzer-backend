package com.example.skillgap.dto.response;

/**
 * Representation of a required skill and its benchmark proficiency level for a role.
 */
public class RoleRequirementResponse {

    private Long skillId;
    private String skillName;
    private String category;
    private Integer requiredLevel;

    public RoleRequirementResponse() {
    }

    public RoleRequirementResponse(Long skillId, String skillName, String category, Integer requiredLevel) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.requiredLevel = requiredLevel;
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

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(Integer requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public static class Builder {
        private Long skillId;
        private String skillName;
        private String category;
        private Integer requiredLevel;

        public Builder skillId(Long skillId) {
            this.skillId = skillId;
            return this;
        }

        public Builder skillName(String skillName) {
            this.skillName = skillName;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder requiredLevel(Integer requiredLevel) {
            this.requiredLevel = requiredLevel;
            return this;
        }

        public RoleRequirementResponse build() {
            return new RoleRequirementResponse(skillId, skillName, category, requiredLevel);
        }
    }
}
