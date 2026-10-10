package com.example.skillgap.repository;

import com.example.skillgap.entity.RoleRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for RoleRequirement entities.
 */
@Repository
public interface RoleRequirementRepository extends JpaRepository<RoleRequirement, Long> {

    /**
     * Retrieve all role requirements for a given role ID.
     *
     * @param roleId the role ID
     * @return list of role requirements with skills loaded
     */
    @Query("SELECT rr FROM RoleRequirement rr JOIN FETCH rr.skill WHERE rr.role.roleId = :roleId")
    List<RoleRequirement> findByRole_RoleId(@Param("roleId") Long roleId);
}
