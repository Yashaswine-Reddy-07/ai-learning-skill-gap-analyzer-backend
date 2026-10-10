package com.example.skillgap.dto.response;

/**
 * Safe public representation of a Student profile excluding sensitive credential details.
 */
public class StudentResponse {

    private Long studentId;
    private String name;
    private String email;
    private String careerGoal;

    public StudentResponse() {
    }

    public StudentResponse(Long studentId, String name, String email, String careerGoal) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.careerGoal = careerGoal;
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
        private Long studentId;
        private String name;
        private String email;
        private String careerGoal;

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

        public StudentResponse build() {
            return new StudentResponse(studentId, name, email, careerGoal);
        }
    }
}
