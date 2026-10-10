package com.example.skillgap.dto.response;

import java.util.List;

/**
 * Composite DTO aggregating student dashboard analytics, skill gaps, and next recommendations.
 */
public class DashboardResponse {

    private StudentResponse student;
    private String targetRole;
    private Long targetRoleId;
    private Double latestQuizScore;
    private Integer overallLevel;
    private List<SkillGapResponse> skillGaps;
    private List<String> recommendedNext;
    private List<LearningPlanItemResponse> learningProgress;
    private Integer completedActivitiesCount;
    private Integer totalActivitiesCount;
    private Double overallProgressPercentage;

    public DashboardResponse() {
    }

    public DashboardResponse(StudentResponse student, String targetRole, Long targetRoleId,
                             Double latestQuizScore, Integer overallLevel, List<SkillGapResponse> skillGaps,
                             List<String> recommendedNext, List<LearningPlanItemResponse> learningProgress,
                             Integer completedActivitiesCount, Integer totalActivitiesCount, Double overallProgressPercentage) {
        this.student = student;
        this.targetRole = targetRole;
        this.targetRoleId = targetRoleId;
        this.latestQuizScore = latestQuizScore;
        this.overallLevel = overallLevel;
        this.skillGaps = skillGaps;
        this.recommendedNext = recommendedNext;
        this.learningProgress = learningProgress;
        this.completedActivitiesCount = completedActivitiesCount;
        this.totalActivitiesCount = totalActivitiesCount;
        this.overallProgressPercentage = overallProgressPercentage;
    }

    public static Builder builder() {
        return new Builder();
    }

    public StudentResponse getStudent() {
        return student;
    }

    public void setStudent(StudentResponse student) {
        this.student = student;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public Long getTargetRoleId() {
        return targetRoleId;
    }

    public void setTargetRoleId(Long targetRoleId) {
        this.targetRoleId = targetRoleId;
    }

    public Double getLatestQuizScore() {
        return latestQuizScore;
    }

    public void setLatestQuizScore(Double latestQuizScore) {
        this.latestQuizScore = latestQuizScore;
    }

    public Integer getOverallLevel() {
        return overallLevel;
    }

    public void setOverallLevel(Integer overallLevel) {
        this.overallLevel = overallLevel;
    }

    public List<SkillGapResponse> getSkillGaps() {
        return skillGaps;
    }

    public void setSkillGaps(List<SkillGapResponse> skillGaps) {
        this.skillGaps = skillGaps;
    }

    public List<String> getRecommendedNext() {
        return recommendedNext;
    }

    public void setRecommendedNext(List<String> recommendedNext) {
        this.recommendedNext = recommendedNext;
    }

    public List<LearningPlanItemResponse> getLearningProgress() {
        return learningProgress;
    }

    public void setLearningProgress(List<LearningPlanItemResponse> learningProgress) {
        this.learningProgress = learningProgress;
    }

    public Integer getCompletedActivitiesCount() {
        return completedActivitiesCount;
    }

    public void setCompletedActivitiesCount(Integer completedActivitiesCount) {
        this.completedActivitiesCount = completedActivitiesCount;
    }

    public Integer getTotalActivitiesCount() {
        return totalActivitiesCount;
    }

    public void setTotalActivitiesCount(Integer totalActivitiesCount) {
        this.totalActivitiesCount = totalActivitiesCount;
    }

    public Double getOverallProgressPercentage() {
        return overallProgressPercentage;
    }

    public void setOverallProgressPercentage(Double overallProgressPercentage) {
        this.overallProgressPercentage = overallProgressPercentage;
    }

    public static class Builder {
        private StudentResponse student;
        private String targetRole;
        private Long targetRoleId;
        private Double latestQuizScore;
        private Integer overallLevel;
        private List<SkillGapResponse> skillGaps;
        private List<String> recommendedNext;
        private List<LearningPlanItemResponse> learningProgress;
        private Integer completedActivitiesCount;
        private Integer totalActivitiesCount;
        private Double overallProgressPercentage;

        public Builder student(StudentResponse student) {
            this.student = student;
            return this;
        }

        public Builder targetRole(String targetRole) {
            this.targetRole = targetRole;
            return this;
        }

        public Builder targetRoleId(Long targetRoleId) {
            this.targetRoleId = targetRoleId;
            return this;
        }

        public Builder latestQuizScore(Double latestQuizScore) {
            this.latestQuizScore = latestQuizScore;
            return this;
        }

        public Builder overallLevel(Integer overallLevel) {
            this.overallLevel = overallLevel;
            return this;
        }

        public Builder skillGaps(List<SkillGapResponse> skillGaps) {
            this.skillGaps = skillGaps;
            return this;
        }

        public Builder recommendedNext(List<String> recommendedNext) {
            this.recommendedNext = recommendedNext;
            return this;
        }

        public Builder learningProgress(List<LearningPlanItemResponse> learningProgress) {
            this.learningProgress = learningProgress;
            return this;
        }

        public Builder completedActivitiesCount(Integer completedActivitiesCount) {
            this.completedActivitiesCount = completedActivitiesCount;
            return this;
        }

        public Builder totalActivitiesCount(Integer totalActivitiesCount) {
            this.totalActivitiesCount = totalActivitiesCount;
            return this;
        }

        public Builder overallProgressPercentage(Double overallProgressPercentage) {
            this.overallProgressPercentage = overallProgressPercentage;
            return this;
        }

        public DashboardResponse build() {
            return new DashboardResponse(student, targetRole, targetRoleId, latestQuizScore, overallLevel,
                    skillGaps, recommendedNext, learningProgress, completedActivitiesCount, totalActivitiesCount, overallProgressPercentage);
        }
    }
}
