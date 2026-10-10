package com.example.skillgap.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Data transfer object representing a student's answer to an individual question.
 */
public class QuizAnswerDto {

    @NotNull(message = "Question ID cannot be null")
    private Long questionId;

    @NotBlank(message = "Selected option cannot be blank")
    @Pattern(regexp = "^[A-D]$", message = "Selected option must be A, B, C, or D")
    private String selectedOption;

    public QuizAnswerDto() {
    }

    public QuizAnswerDto(Long questionId, String selectedOption) {
        this.questionId = questionId;
        this.selectedOption = selectedOption;
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

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public static class Builder {
        private Long questionId;
        private String selectedOption;

        public Builder questionId(Long questionId) {
            this.questionId = questionId;
            return this;
        }

        public Builder selectedOption(String selectedOption) {
            this.selectedOption = selectedOption;
            return this;
        }

        public QuizAnswerDto build() {
            return new QuizAnswerDto(questionId, selectedOption);
        }
    }
}
