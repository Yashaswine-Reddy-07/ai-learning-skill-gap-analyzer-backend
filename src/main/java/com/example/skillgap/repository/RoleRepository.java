package com.example.skillgap.repository;

import com.example.skillgap.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for Role entities.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find a career role by its unique role name.
     *
     * @param roleName the name of the role
     * @return Optional containing the role if found
     */
    Optional<Role> findByRoleName(String roleName);
}
