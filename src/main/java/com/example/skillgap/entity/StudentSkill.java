package com.example.skillgap.entity;

import jakarta.persistence.*;

/**
 * Entity tracking the current assessed proficiency (1 to 5) of a student in a specific skill.
 * Proficiency is objectively populated from quiz results rather than self-ratings.
 */
@Entity
@Table(name = "student_skills")
public class StudentSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_skill_id")
    private Long studentSkillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "proficiency", nullable = false)
    private Integer proficiency;

    public StudentSkill() {
    }

    public StudentSkill(Long studentSkillId, Student student, Skill skill, Integer proficiency) {
        this.studentSkillId = studentSkillId;
        this.student = student;
        this.skill = skill;
        this.proficiency = proficiency;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getStudentSkillId() {
        return studentSkillId;
    }

    public void setStudentSkillId(Long studentSkillId) {
        this.studentSkillId = studentSkillId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Integer getProficiency() {
        return proficiency;
    }

    public void setProficiency(Integer proficiency) {
        this.proficiency = proficiency;
    }

    public static class Builder {
        private Long studentSkillId;
        private Student student;
        private Skill skill;
        private Integer proficiency;

        public Builder studentSkillId(Long studentSkillId) {
            this.studentSkillId = studentSkillId;
            return this;
        }

        public Builder student(Student student) {
            this.student = student;
            return this;
        }

        public Builder skill(Skill skill) {
            this.skill = skill;
            return this;
        }

        public Builder proficiency(Integer proficiency) {
            this.proficiency = proficiency;
            return this;
        }

        public StudentSkill build() {
            return new StudentSkill(studentSkillId, student, skill, proficiency);
        }
    }
}
