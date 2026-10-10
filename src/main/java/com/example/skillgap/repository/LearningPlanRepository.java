package com.example.skillgap.repository;

import com.example.skillgap.entity.LearningPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for LearningPlan entities.
 */
@Repository
public interface LearningPlanRepository extends JpaRepository<LearningPlan, Long> {

    /**
     * Retrieve all learning plan items for a student ordered by priority.
     *
     * @param studentId the student ID
     * @return list of learning plan items
     */
    @Query("SELECT lp FROM LearningPlan lp WHERE lp.student.studentId = :studentId ORDER BY lp.priority ASC, lp.planId ASC")
    List<LearningPlan> findByStudent_StudentIdOrderByPriorityAsc(@Param("studentId") Long studentId);

    /**
     * Delete existing learning plan items for a student before regenerating.
     *
     * @param studentId the student ID
     */
    void deleteByStudent_StudentId(Long studentId);
}
