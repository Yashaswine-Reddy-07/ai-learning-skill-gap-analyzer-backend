package com.example.skillgap.dto.response;

import java.time.LocalDateTime;

/**
 * Single actionable item or milestone in the personalized learning curriculum.
 */
public class LearningPlanItemResponse {

    private Long planId;
    private Long studentId;
    private String skill;
    private String topic;
    private String activity;
    private Integer priority;
    private Integer progress;
    private String status;
    private LocalDateTime createdDate;

    public LearningPlanItemResponse() {
    }

    public LearningPlanItemResponse(Long planId, Long studentId, String skill, String topic,
                                    String activity, Integer priority, Integer progress,
                                    String status, LocalDateTime createdDate) {
        this.planId = planId;
        this.studentId = studentId;
        this.skill = skill;
        this.topic = topic;
        this.activity = activity;
        this.priority = priority;
        this.progress = progress;
        this.status = status;
        this.createdDate = createdDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public static class Builder {
        private Long planId;
        private Long studentId;
        private String skill;
        private String topic;
        private String activity;
        private Integer priority;
        private Integer progress;
        private String status;
        private LocalDateTime createdDate;

        public Builder planId(Long planId) {
            this.planId = planId;
            return this;
        }

        public Builder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder skill(String skill) {
            this.skill = skill;
            return this;
        }

        public Builder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public Builder activity(String activity) {
            this.activity = activity;
            return this;
        }

        public Builder priority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public Builder progress(Integer progress) {
            this.progress = progress;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public LearningPlanItemResponse build() {
            return new LearningPlanItemResponse(planId, studentId, skill, topic, activity, priority, progress, status, createdDate);
        }
    }
}
