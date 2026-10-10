package com.example.skillgap.entity;

import jakarta.persistence.*;

/**
 * Entity representing an objective multiple-choice question in the question bank.
 * Note: 'correctOption' must remain internal to the backend and never exposed to the frontend.
 */
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "option_a", nullable = false, length = 500)
    private String optionA;

    @Column(name = "option_b", nullable = false, length = 500)
    private String optionB;

    @Column(name = "option_c", nullable = false, length = 500)
    private String optionC;

    @Column(name = "option_d", nullable = false, length = 500)
    private String optionD;

    @Column(name = "correct_option", nullable = false, length = 5)
    private String correctOption;

    @Column(name = "difficulty", nullable = false, length = 20)
    private String difficulty;

    @Column(name = "topic", nullable = false, length = 100)
    private String topic;

    public Question() {
    }

    public Question(Long questionId, Skill skill, String questionText, String optionA, String optionB,
                    String optionC, String optionD, String correctOption, String difficulty, String topic) {
        this.questionId = questionId;
        this.skill = skill;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
        this.difficulty = difficulty;
        this.topic = topic;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public static class Builder {
        private Long questionId;
        private Skill skill;
        private String questionText;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private String correctOption;
        private String difficulty;
        private String topic;

        public Builder questionId(Long questionId) {
            this.questionId = questionId;
            return this;
        }

        public Builder skill(Skill skill) {
            this.skill = skill;
            return this;
        }

        public Builder questionText(String questionText) {
            this.questionText = questionText;
            return this;
        }

        public Builder optionA(String optionA) {
            this.optionA = optionA;
            return this;
        }

        public Builder optionB(String optionB) {
            this.optionB = optionB;
            return this;
        }

        public Builder optionC(String optionC) {
            this.optionC = optionC;
            return this;
        }

        public Builder optionD(String optionD) {
            this.optionD = optionD;
            return this;
        }

        public Builder correctOption(String correctOption) {
            this.correctOption = correctOption;
            return this;
        }

        public Builder difficulty(String difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public Builder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public Question build() {
            return new Question(questionId, skill, questionText, optionA, optionB, optionC, optionD, correctOption, difficulty, topic);
        }
    }
}
