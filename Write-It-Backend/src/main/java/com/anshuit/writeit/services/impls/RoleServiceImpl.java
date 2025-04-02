package com.anshuit.writeit.services.impls;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anshuit.writeit.entities.Role;
import com.anshuit.writeit.exceptions.CustomException;
import com.anshuit.writeit.exceptions.enums.ExceptionDetailsEnum;
import com.anshuit.writeit.repositories.RoleRepository;
import com.anshuit.writeit.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Role saveOrUpdateRole(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public Optional<Role> getRoleByIdOptional(int roleId) {
		return roleRepository.findById(roleId);
	}

	@Override
	public Optional<Role> getRoleByRoleNameOptional(String roleName) {
		return roleRepository.findByRoleName(roleName);
	}

	@Override
	public Role getRoleByRoleName(String roleName) {
		Role foundRole = this.getRoleByRoleNameOptional(roleName)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,
						ExceptionDetailsEnum.ROLE_NOT_FOUND_WITH_ROLE_NAME, roleName));
		return foundRole;
	}

}
