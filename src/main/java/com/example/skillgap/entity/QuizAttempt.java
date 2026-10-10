package com.example.skillgap.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity tracking a student's attempt at taking a role-based skill assessment.
 */
@Entity
@Table(name = "quiz_attempts")
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    private Long attemptId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "attempt_date", nullable = false)
    private LocalDateTime attemptDate;

    @Column(name = "total_score", nullable = false)
    private Double totalScore;

    public QuizAttempt() {
    }

    public QuizAttempt(Long attemptId, Student student, Role role, LocalDateTime attemptDate, Double totalScore) {
        this.attemptId = attemptId;
        this.student = student;
        this.role = role;
        this.attemptDate = attemptDate;
        this.totalScore = totalScore;
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getAttemptDate() {
        return attemptDate;
    }

    public void setAttemptDate(LocalDateTime attemptDate) {
        this.attemptDate = attemptDate;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public static class Builder {
        private Long attemptId;
        private Student student;
        private Role role;
        private LocalDateTime attemptDate;
        private Double totalScore;

        public Builder attemptId(Long attemptId) {
            this.attemptId = attemptId;
            return this;
        }

        public Builder student(Student student) {
            this.student = student;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder attemptDate(LocalDateTime attemptDate) {
            this.attemptDate = attemptDate;
            return this;
        }

        public Builder totalScore(Double totalScore) {
            this.totalScore = totalScore;
            return this;
        }

        public QuizAttempt build() {
            return new QuizAttempt(attemptId, student, role, attemptDate, totalScore);
        }
    }
}
