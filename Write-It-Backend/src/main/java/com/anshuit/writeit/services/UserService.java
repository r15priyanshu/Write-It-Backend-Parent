package com.anshuit.writeit.services;

import java.util.List;
import java.util.Optional;

import com.anshuit.writeit.dto.AppUserDto;
import com.anshuit.writeit.entities.AppUser;

public interface UserService {
	AppUser createUser(AppUser user);

	AppUser updateUserByUserId(AppUserDto userDto, int userId);

	Optional<AppUser> getUserByIdOptional(int userId);

	AppUser getUserById(int userId);

	boolean deleteUserByUserId(int userId);

	boolean deleteAllUsers();

	List<AppUser> getAllUsers();
}
