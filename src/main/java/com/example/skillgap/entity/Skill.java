package com.example.skillgap.entity;

import jakarta.persistence.*;

/**
 * Entity representing an individual technical skill evaluated by the system.
 */
@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Long skillId;

    @Column(name = "skill_name", nullable = false, unique = true, length = 100)
    private String skillName;

    @Column(name = "category", nullable = false, length = 100)
    private String category;

    public Skill() {
    }

    public Skill(Long skillId, String skillName, String category) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
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

    public static class Builder {
        private Long skillId;
        private String skillName;
        private String category;

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

        public Skill build() {
            return new Skill(skillId, skillName, category);
        }
    }
}
