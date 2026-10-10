package com.example.skillgap.service;

import com.example.skillgap.dto.response.RoleRequirementResponse;
import com.example.skillgap.dto.response.RoleResponse;
import com.example.skillgap.entity.Role;
import com.example.skillgap.entity.RoleRequirement;
import com.example.skillgap.exception.ResourceNotFoundException;
import com.example.skillgap.repository.RoleRepository;
import com.example.skillgap.repository.RoleRequirementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service managing career role lookups and their skill proficiency benchmarks.
 */
@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;
    private final RoleRequirementRepository roleRequirementRepository;

    public RoleService(RoleRepository roleRepository,
                       RoleRequirementRepository roleRequirementRepository) {
        this.roleRepository = roleRepository;
        this.roleRequirementRepository = roleRequirementRepository;
    }

    /**
     * Retrieves all available career roles along with their required skills and levels.
     *
     * @return list of RoleResponse DTOs
     */
    @Transactional(readOnly = true)
    public List<RoleResponse> getAllRoles() {
        try {
            List<Role> roles = roleRepository.findAll();
            List<RoleResponse> responseList = new ArrayList<>();

            for (Role role : roles) {
                responseList.add(mapToRoleResponse(role));
            }

            return responseList;
        } catch (Exception ex) {
            System.err.println("RoleService: Error in getAllRoles: " + ex.getMessage());
            logger.error("Failed to retrieve all roles", ex);
            throw new RuntimeException("Error fetching career roles", ex);
        }
    }

    /**
     * Retrieves a single career role and its skill requirements by role ID.
     *
     * @param roleId the role primary key
     * @return RoleResponse DTO
     */
    @Transactional(readOnly = true)
    public RoleResponse getRoleById(Long roleId) {
        try {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> {
                        System.err.println("RoleService: Role not found with ID: " + roleId);
                        return new ResourceNotFoundException("Role not found with ID: " + roleId);
                    });

            return mapToRoleResponse(role);
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("RoleService: Error in getRoleById: " + ex.getMessage());
            logger.error("Failed to retrieve role with ID: {}", roleId, ex);
            throw new RuntimeException("Error fetching role details", ex);
        }
    }

    /**
     * Helper mapping a Role entity and its RoleRequirement records to a RoleResponse DTO.
     *
     * @param role the Role entity
     * @return populated RoleResponse
     */
    private RoleResponse mapToRoleResponse(Role role) {
        try {
            List<RoleRequirement> requirements = roleRequirementRepository.findByRole_RoleId(role.getRoleId());
            List<RoleRequirementResponse> reqResponses = requirements.stream()
                    .map(r -> RoleRequirementResponse.builder()
                            .skillId(r.getSkill().getSkillId())
                            .skillName(r.getSkill().getSkillName())
                            .category(r.getSkill().getCategory())
                            .requiredLevel(r.getRequiredLevel())
                            .build())
                    .toList();

            return RoleResponse.builder()
                    .roleId(role.getRoleId())
                    .roleName(role.getRoleName())
                    .requiredSkills(reqResponses)
                    .build();
        } catch (Exception ex) {
            System.err.println("RoleService: Error mapping role response: " + ex.getMessage());
            logger.error("Failed to map role entity to response DTO", ex);
            return RoleResponse.builder()
                    .roleId(role.getRoleId())
                    .roleName(role.getRoleName())
                    .requiredSkills(List.of())
                    .build();
        }
    }
}
