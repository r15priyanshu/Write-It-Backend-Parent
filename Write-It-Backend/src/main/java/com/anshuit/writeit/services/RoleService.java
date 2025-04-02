package com.anshuit.writeit.services;

import java.util.Optional;

import com.anshuit.writeit.entities.Role;

public interface RoleService {
	Role saveOrUpdateRole(Role role);

	Optional<Role> getRoleByIdOptional(int roleId);

	Optional<Role> getRoleByRoleNameOptional(String roleName);

	Role getRoleByRoleName(String roleName);
}
