package com.example.skillgap.dto.response;

import java.util.List;

/**
 * Representation of a career role with its associated skill requirements.
 */
public class RoleResponse {

    private Long roleId;
    private String roleName;
    private List<RoleRequirementResponse> requiredSkills;

    public RoleResponse() {
    }

    public RoleResponse(Long roleId, String roleName, List<RoleRequirementResponse> requiredSkills) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.requiredSkills = requiredSkills;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<RoleRequirementResponse> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<RoleRequirementResponse> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public static class Builder {
        private Long roleId;
        private String roleName;
        private List<RoleRequirementResponse> requiredSkills;

        public Builder roleId(Long roleId) {
            this.roleId = roleId;
            return this;
        }

        public Builder roleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public Builder requiredSkills(List<RoleRequirementResponse> requiredSkills) {
            this.requiredSkills = requiredSkills;
            return this;
        }

        public RoleResponse build() {
            return new RoleResponse(roleId, roleName, requiredSkills);
        }
    }
}
