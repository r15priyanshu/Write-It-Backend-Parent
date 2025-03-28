package com.anshuit.writeit.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anshuit.writeit.entities.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Integer>{
	Optional<AppUser> findUserByUsername(String username);
	Optional<AppUser> findUserByUsernameAndPassword(String username,String password);

}
