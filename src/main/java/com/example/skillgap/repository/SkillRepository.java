package com.example.skillgap.repository;

import com.example.skillgap.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for Skill entities.
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    /**
     * Find a skill by its unique name.
     *
     * @param skillName the name of the skill
     * @return Optional containing the skill if found
     */
    Optional<Skill> findBySkillName(String skillName);
}
