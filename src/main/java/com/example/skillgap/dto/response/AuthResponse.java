package com.example.skillgap.dto.response;

/**
 * Response payload returned upon successful student registration or login.
 */
public class AuthResponse {

    private String token;
    private Long studentId;
    private String name;
    private String email;
    private String careerGoal;

    public AuthResponse() {
    }

    public AuthResponse(String token, Long studentId, String name, String email, String careerGoal) {
        this.token = token;
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.careerGoal = careerGoal;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCareerGoal() {
        return careerGoal;
    }

    public void setCareerGoal(String careerGoal) {
        this.careerGoal = careerGoal;
    }

    public static class Builder {
        private String token;
        private Long studentId;
        private String name;
        private String email;
        private String careerGoal;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder careerGoal(String careerGoal) {
            this.careerGoal = careerGoal;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(token, studentId, name, email, careerGoal);
        }
    }
}
