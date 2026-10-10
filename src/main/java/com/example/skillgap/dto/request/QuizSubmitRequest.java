package com.example.skillgap.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Request payload containing all student answers for evaluating a career role quiz.
 */
public class QuizSubmitRequest {

    @NotNull(message = "Student ID cannot be null")
    private Long studentId;

    @NotNull(message = "Role ID cannot be null")
    private Long roleId;

    @NotEmpty(message = "Answers list cannot be empty")
    @Valid
    private List<QuizAnswerDto> answers;

    public QuizSubmitRequest() {
    }

    public QuizSubmitRequest(Long studentId, Long roleId, List<QuizAnswerDto> answers) {
        this.studentId = studentId;
        this.roleId = roleId;
        this.answers = answers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<QuizAnswerDto> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuizAnswerDto> answers) {
        this.answers = answers;
    }

    public static class Builder {
        private Long studentId;
        private Long roleId;
        private List<QuizAnswerDto> answers;

        public Builder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder roleId(Long roleId) {
            this.roleId = roleId;
            return this;
        }

        public Builder answers(List<QuizAnswerDto> answers) {
            this.answers = answers;
            return this;
        }

        public QuizSubmitRequest build() {
            return new QuizSubmitRequest(studentId, roleId, answers);
        }
    }
}
