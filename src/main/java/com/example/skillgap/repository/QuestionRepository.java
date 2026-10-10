package com.example.skillgap.repository;

import com.example.skillgap.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for Question entities.
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    /**
     * Retrieve all questions for a specific skill.
     *
     * @param skillId the skill ID
     * @return list of questions belonging to that skill
     */
    @Query("SELECT q FROM Question q JOIN FETCH q.skill WHERE q.skill.skillId = :skillId")
    List<Question> findBySkill_SkillId(@Param("skillId") Long skillId);

    /**
     * Retrieve all questions matching any of the provided skill IDs.
     *
     * @param skillIds list of skill IDs
     * @return list of questions belonging to those skills
     */
    @Query("SELECT q FROM Question q JOIN FETCH q.skill WHERE q.skill.skillId IN :skillIds")
    List<Question> findBySkill_SkillIdIn(@Param("skillIds") List<Long> skillIds);
}
