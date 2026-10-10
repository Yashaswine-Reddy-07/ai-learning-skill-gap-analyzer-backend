package com.example.skillgap.repository;

import com.example.skillgap.entity.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for QuizResult entities.
 */
@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {

    /**
     * Retrieve all skill results associated with a quiz attempt ID.
     *
     * @param attemptId the attempt ID
     * @return list of quiz results with skill details loaded
     */
    @Query("SELECT qr FROM QuizResult qr JOIN FETCH qr.skill WHERE qr.attempt.attemptId = :attemptId")
    List<QuizResult> findByAttempt_AttemptId(@Param("attemptId") Long attemptId);
}
