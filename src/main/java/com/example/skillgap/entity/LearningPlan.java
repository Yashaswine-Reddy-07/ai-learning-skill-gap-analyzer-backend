package com.example.skillgap.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing an actionable learning plan item generated based on skill gaps.
 * Contains skill, topic, activity, priority, progress percentage, and status.
 */
@Entity
@Table(name = "learning_plans")
public class LearningPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long planId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "skill", nullable = false, length = 100)
    private String skill;

    @Column(name = "topic", nullable = false, length = 100)
    private String topic;

    @Column(name = "activity", nullable = false, columnDefinition = "TEXT")
    private String activity;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "progress", nullable = false)
    private Integer progress = 0;

    @Column(name = "status", nullable = false, length = 30)
    private String status = "NOT_STARTED"; // NOT_STARTED, IN_PROGRESS, COMPLETED

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    public LearningPlan() {
    }

    public LearningPlan(Long planId, Student student, String skill, String topic, String activity,
                        Integer priority, Integer progress, String status, LocalDateTime createdDate) {
        this.planId = planId;
        this.student = student;
        this.skill = skill;
        this.topic = topic;
        this.activity = activity;
        this.priority = priority;
        this.progress = progress != null ? progress : 0;
        this.status = status != null ? status : "NOT_STARTED";
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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
        private Student student;
        private String skill;
        private String topic;
        private String activity;
        private Integer priority;
        private Integer progress = 0;
        private String status = "NOT_STARTED";
        private LocalDateTime createdDate;

        public Builder planId(Long planId) {
            this.planId = planId;
            return this;
        }

        public Builder student(Student student) {
            this.student = student;
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

        public LearningPlan build() {
            return new LearningPlan(planId, student, skill, topic, activity, priority, progress, status, createdDate);
        }
    }
}
