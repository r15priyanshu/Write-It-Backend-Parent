package com.anshuit.writeit.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anshuit.writeit.constants.GlobalConstants;
import com.anshuit.writeit.dto.AppUserDto;
import com.anshuit.writeit.entities.AppUser;
import com.anshuit.writeit.entities.Role;
import com.anshuit.writeit.exceptions.CustomException;
import com.anshuit.writeit.repositories.RoleRepository;
import com.anshuit.writeit.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public AppUser createUser(AppUser user) {
		if (userRepository.findUserByUsername(user.getUsername().toLowerCase()).isPresent())
			throw new CustomException("Username already taken : " + (user.getUsername().toLowerCase()),
					HttpStatus.CONFLICT);
		user.setUsername(user.getUsername().toLowerCase());
		ArrayList<Role> roles = new ArrayList<>();
		roles.add(roleRepository.findByRoleName(GlobalConstants.DEFAULT_ROLE_ONE));
		user.setRoles(roles);
		user.setProfilePic(GlobalConstants.DEFAULT_PROFILE_IMAGE_NAME);
		return userRepository.save(user);
	}

	@Override
	public AppUser updateUserByUserId(AppUserDto userDto, int userId) {
		AppUser foundUser = this.getUserById(userId);

		if (userDto.getUsername() != null) {
			if (userRepository.findUserByUsername(userDto.getUsername().toLowerCase()).isPresent())
				throw new CustomException("Username already taken : " + (userDto.getUsername().toLowerCase()),
						HttpStatus.CONFLICT);
		}

		foundUser.setName(userDto.getName() == null ? foundUser.getName() : userDto.getName());
		foundUser.setUsername(userDto.getUsername() == null ? foundUser.getUsername().toLowerCase()
				: userDto.getUsername().toLowerCase());
		foundUser.setPassword(userDto.getPassword() == null ? foundUser.getPassword() : userDto.getPassword());
		foundUser.setAbout(userDto.getAbout() == null ? foundUser.getAbout() : userDto.getAbout());
		AppUser updateduser = userRepository.save(foundUser);
		return updateduser;
	}

	@Override
	public AppUser getUserById(int userId) {
		AppUser foundUser = userRepository.findById(userId)
				.orElseThrow(() -> new CustomException("User Not Found with userId : " + userId, HttpStatus.NOT_FOUND));
		return foundUser;
	}

	@Override
	public List<AppUser> getAllUsers() {
		List<AppUser> allusers = userRepository.findAll();
		if (allusers.size() == 0)
			throw new CustomException("No users found in DB !!", HttpStatus.NOT_FOUND);

		return allusers;
	}

	@Override
	public boolean deleteUserByUserId(int userId) {
		AppUser foundUser = this.getUserById(userId);
		userRepository.delete(foundUser);
		return true;
	}

	@Override
	public boolean deleteAllUsers() {
		userRepository.deleteAll();
		return true;
	}

	@Override
	public Optional<AppUser> getUserByIdOptional(int userId) {
		return userRepository.findById(userId);
	}
}
