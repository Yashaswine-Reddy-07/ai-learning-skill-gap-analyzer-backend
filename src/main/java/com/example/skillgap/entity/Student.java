package com.example.skillgap.entity;

import jakarta.persistence.*;

/**
 * Entity representing a Student in the Skill Gap Analyzer system.
 * Stores authentication credentials, identity, and career aspirations.
 */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "career_goal", length = 100)
    private String careerGoal;

    public Student() {
    }

    public Student(Long studentId, String name, String email, String password, String careerGoal) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        private String password;
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

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder careerGoal(String careerGoal) {
            this.careerGoal = careerGoal;
            return this;
        }

        public Student build() {
            return new Student(studentId, name, email, password, careerGoal);
        }
    }
}
