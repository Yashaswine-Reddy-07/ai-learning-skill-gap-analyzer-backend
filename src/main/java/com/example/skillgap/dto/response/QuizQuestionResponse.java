package com.example.skillgap.dto.response;

/**
 * Public quiz question presented to students.
 * STRICT SECURITY RULE: correctOption is omitted to prevent client-side answer inspection.
 */
public class QuizQuestionResponse {

    private Long questionId;
    private Long skillId;
    private String skillName;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String difficulty;
    private String topic;

    public QuizQuestionResponse() {
    }

    public QuizQuestionResponse(Long questionId, Long skillId, String skillName, String questionText,
                                String optionA, String optionB, String optionC, String optionD,
                                String difficulty, String topic) {
        this.questionId = questionId;
        this.skillId = skillId;
        this.skillName = skillName;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
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
        private Long skillId;
        private String skillName;
        private String questionText;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private String difficulty;
        private String topic;

        public Builder questionId(Long questionId) {
            this.questionId = questionId;
            return this;
        }

        public Builder skillId(Long skillId) {
            this.skillId = skillId;
            return this;
        }

        public Builder skillName(String skillName) {
            this.skillName = skillName;
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

        public Builder difficulty(String difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public Builder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public QuizQuestionResponse build() {
            return new QuizQuestionResponse(questionId, skillId, skillName, questionText, optionA, optionB, optionC, optionD, difficulty, topic);
        }
    }
}
