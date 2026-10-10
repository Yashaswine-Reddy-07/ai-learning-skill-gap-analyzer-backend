package com.example.skillgap.repository;

import com.example.skillgap.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for QuizAttempt entities.
 */
@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    /**
     * Retrieve all quiz attempts by a student, sorted by date descending.
     *
     * @param studentId the student ID
     * @return list of quiz attempts
     */
    @Query("SELECT qa FROM QuizAttempt qa JOIN FETCH qa.role WHERE qa.student.studentId = :studentId ORDER BY qa.attemptDate DESC")
    List<QuizAttempt> findByStudent_StudentIdOrderByAttemptDateDesc(@Param("studentId") Long studentId);

    /**
     * Retrieve the most recent quiz attempt by a student.
     *
     * @param studentId the student ID
     * @return Optional containing the latest attempt
     */
    Optional<QuizAttempt> findFirstByStudent_StudentIdOrderByAttemptDateDesc(Long studentId);

    /**
     * Retrieve the most recent quiz attempt by a student for a specific role.
     *
     * @param studentId the student ID
     * @param roleId    the role ID
     * @return Optional containing the latest attempt for that role
     */
    Optional<QuizAttempt> findFirstByStudent_StudentIdAndRole_RoleIdOrderByAttemptDateDesc(Long studentId, Long roleId);
}
