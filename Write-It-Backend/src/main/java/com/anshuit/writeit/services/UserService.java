package com.anshuit.writeit.services;

import java.util.List;
import java.util.Optional;

import com.anshuit.writeit.dto.AppUserDto;
import com.anshuit.writeit.entities.AppUser;

public interface UserService {
	AppUser saveOrUpdateUser(AppUser user);

	AppUser createUser(AppUser user);

	Optional<AppUser> getUserByIdOptional(int userId);

	AppUser getUserById(int userId);

	Optional<AppUser> getUserByUsernameOptional(String username);

	List<AppUser> getAllUsers();

	AppUser updateUserByUserId(AppUserDto userDto, int userId);

	boolean deleteUserByUserId(int userId);

	boolean deleteAllUsers();
}
