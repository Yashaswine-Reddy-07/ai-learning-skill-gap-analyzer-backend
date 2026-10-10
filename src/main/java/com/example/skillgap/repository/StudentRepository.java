package com.example.skillgap.repository;

import com.example.skillgap.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for Student entities.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Find a student by their unique email address.
     *
     * @param email the student email
     * @return Optional containing the student if found
     */
    Optional<Student> findByEmail(String email);

    /**
     * Check whether an email already exists in the system.
     *
     * @param email the email to verify
     * @return true if existing, false otherwise
     */
    boolean existsByEmail(String email);
}
