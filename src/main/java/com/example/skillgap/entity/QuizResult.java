package com.example.skillgap.entity;

import jakarta.persistence.*;

/**
 * Entity recording the granular evaluation outcome per skill for a given quiz attempt.
 */
@Entity
@Table(name = "quiz_results")
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private QuizAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "correct_count", nullable = false)
    private Integer correctCount;

    @Column(name = "total_questions", nullable = false)
    private Integer totalQuestions;

    @Column(name = "level", nullable = false)
    private Integer level;

    public QuizResult() {
    }

    public QuizResult(Long resultId, QuizAttempt attempt, Skill skill, Integer correctCount, Integer totalQuestions, Integer level) {
        this.resultId = resultId;
        this.attempt = attempt;
        this.skill = skill;
        this.correctCount = correctCount;
        this.totalQuestions = totalQuestions;
        this.level = level;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public QuizAttempt getAttempt() {
        return attempt;
    }

    public void setAttempt(QuizAttempt attempt) {
        this.attempt = attempt;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Integer getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public static class Builder {
        private Long resultId;
        private QuizAttempt attempt;
        private Skill skill;
        private Integer correctCount;
        private Integer totalQuestions;
        private Integer level;

        public Builder resultId(Long resultId) {
            this.resultId = resultId;
            return this;
        }

        public Builder attempt(QuizAttempt attempt) {
            this.attempt = attempt;
            return this;
        }

        public Builder skill(Skill skill) {
            this.skill = skill;
            return this;
        }

        public Builder correctCount(Integer correctCount) {
            this.correctCount = correctCount;
            return this;
        }

        public Builder totalQuestions(Integer totalQuestions) {
            this.totalQuestions = totalQuestions;
            return this;
        }

        public Builder level(Integer level) {
            this.level = level;
            return this;
        }

        public QuizResult build() {
            return new QuizResult(resultId, attempt, skill, correctCount, totalQuestions, level);
        }
    }
}
