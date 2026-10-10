package com.example.skillgap.repository;

import com.example.skillgap.entity.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for StudentSkill entities.
 */
@Repository
public interface StudentSkillRepository extends JpaRepository<StudentSkill, Long> {

    /**
     * Retrieve all skill proficiencies recorded for a student.
     *
     * @param studentId the student ID
     * @return list of student skills with skill entities joined
     */
    @Query("SELECT ss FROM StudentSkill ss JOIN FETCH ss.skill WHERE ss.student.studentId = :studentId")
    List<StudentSkill> findByStudent_StudentId(@Param("studentId") Long studentId);

    /**
     * Find a student's assessed skill level for a specific skill.
     *
     * @param studentId the student ID
     * @param skillId   the skill ID
     * @return Optional containing the StudentSkill if found
     */
    Optional<StudentSkill> findByStudent_StudentIdAndSkill_SkillId(Long studentId, Long skillId);
}
