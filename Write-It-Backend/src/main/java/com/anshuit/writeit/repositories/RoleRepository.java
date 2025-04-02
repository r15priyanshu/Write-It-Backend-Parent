package com.anshuit.writeit.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anshuit.writeit.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByRoleName(String name);
}
