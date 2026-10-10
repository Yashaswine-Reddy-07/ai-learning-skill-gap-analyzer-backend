package com.example.skillgap.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Complete evaluation report returned upon quiz completion or attempt lookup.
 */
public class QuizResultResponse {

    private Long attemptId;
    private Long roleId;
    private String roleName;
    private Double overallScore;
    private Integer overallLevel;
    private LocalDateTime attemptDate;
    private List<SkillResultDto> skills;

    public QuizResultResponse() {
    }

    public QuizResultResponse(Long attemptId, Long roleId, String roleName, Double overallScore,
                              Integer overallLevel, LocalDateTime attemptDate, List<SkillResultDto> skills) {
        this.attemptId = attemptId;
        this.roleId = roleId;
        this.roleName = roleName;
        this.overallScore = overallScore;
        this.overallLevel = overallLevel;
        this.attemptDate = attemptDate;
        this.skills = skills;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }

    public Integer getOverallLevel() {
        return overallLevel;
    }

    public void setOverallLevel(Integer overallLevel) {
        this.overallLevel = overallLevel;
    }

    public LocalDateTime getAttemptDate() {
        return attemptDate;
    }

    public void setAttemptDate(LocalDateTime attemptDate) {
        this.attemptDate = attemptDate;
    }

    public List<SkillResultDto> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillResultDto> skills) {
        this.skills = skills;
    }

    public static class Builder {
        private Long attemptId;
        private Long roleId;
        private String roleName;
        private Double overallScore;
        private Integer overallLevel;
        private LocalDateTime attemptDate;
        private List<SkillResultDto> skills;

        public Builder attemptId(Long attemptId) {
            this.attemptId = attemptId;
            return this;
        }

        public Builder roleId(Long roleId) {
            this.roleId = roleId;
            return this;
        }

        public Builder roleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public Builder overallScore(Double overallScore) {
            this.overallScore = overallScore;
            return this;
        }

        public Builder overallLevel(Integer overallLevel) {
            this.overallLevel = overallLevel;
            return this;
        }

        public Builder attemptDate(LocalDateTime attemptDate) {
            this.attemptDate = attemptDate;
            return this;
        }

        public Builder skills(List<SkillResultDto> skills) {
            this.skills = skills;
            return this;
        }

        public QuizResultResponse build() {
            return new QuizResultResponse(attemptId, roleId, roleName, overallScore, overallLevel, attemptDate, skills);
        }
    }
}
