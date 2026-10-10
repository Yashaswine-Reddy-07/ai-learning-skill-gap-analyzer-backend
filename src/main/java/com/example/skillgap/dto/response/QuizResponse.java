package com.example.skillgap.dto.response;

import java.util.List;

/**
 * Encapsulation of a freshly generated role-based quiz session.
 */
public class QuizResponse {

    private Long roleId;
    private String roleName;
    private Integer totalQuestions;
    private List<QuizQuestionResponse> questions;

    public QuizResponse() {
    }

    public QuizResponse(Long roleId, String roleName, Integer totalQuestions, List<QuizQuestionResponse> questions) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.totalQuestions = totalQuestions;
        this.questions = questions;
    }

    public static Builder builder() {
        return new Builder();
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

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public List<QuizQuestionResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizQuestionResponse> questions) {
        this.questions = questions;
    }

    public static class Builder {
        private Long roleId;
        private String roleName;
        private Integer totalQuestions;
        private List<QuizQuestionResponse> questions;

        public Builder roleId(Long roleId) {
            this.roleId = roleId;
            return this;
        }

        public Builder roleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public Builder totalQuestions(Integer totalQuestions) {
            this.totalQuestions = totalQuestions;
            return this;
        }

        public Builder questions(List<QuizQuestionResponse> questions) {
            this.questions = questions;
            return this;
        }

        public QuizResponse build() {
            return new QuizResponse(roleId, roleName, totalQuestions, questions);
        }
    }
}
