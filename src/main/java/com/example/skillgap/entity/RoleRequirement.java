package com.example.skillgap.entity;

import jakarta.persistence.*;

/**
 * Entity mapping the proficiency requirements of a specific role to skills.
 * Defines the required level (1 to 5) for benchmark evaluation.
 */
@Entity
@Table(name = "role_requirements")
public class RoleRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requirement_id")
    private Long requirementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "required_level", nullable = false)
    private Integer requiredLevel;

    public RoleRequirement() {
    }

    public RoleRequirement(Long requirementId, Role role, Skill skill, Integer requiredLevel) {
        this.requirementId = requirementId;
        this.role = role;
        this.skill = skill;
        this.requiredLevel = requiredLevel;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Long requirementId) {
        this.requirementId = requirementId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Integer getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(Integer requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public static class Builder {
        private Long requirementId;
        private Role role;
        private Skill skill;
        private Integer requiredLevel;

        public Builder requirementId(Long requirementId) {
            this.requirementId = requirementId;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder skill(Skill skill) {
            this.skill = skill;
            return this;
        }

        public Builder requiredLevel(Integer requiredLevel) {
            this.requiredLevel = requiredLevel;
            return this;
        }

        public RoleRequirement build() {
            return new RoleRequirement(requirementId, role, skill, requiredLevel);
        }
    }
}
