package com.anshuit.writeit.services.impls;

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
import com.anshuit.writeit.exceptions.enums.ExceptionDetailsEnum;
import com.anshuit.writeit.repositories.UserRepository;
import com.anshuit.writeit.services.RoleService;
import com.anshuit.writeit.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleService roleService;

	@Override
	public AppUser saveOrUpdateUser(AppUser user) {
		return userRepository.save(user);
	}

	@Override
	public AppUser createUser(AppUser user) {
		if (this.getUserByUsernameOptional(user.getUsername().toLowerCase()).isPresent())
			throw new CustomException(HttpStatus.CONFLICT, ExceptionDetailsEnum.USERNAME_ALREADY_TAKEN,
					user.getUsername());

		user.setUsername(user.getUsername().toLowerCase());
		ArrayList<Role> roles = new ArrayList<>();
		roles.add(roleService.getRoleByRoleName(GlobalConstants.DEFAULT_ROLE_ONE));
		user.setRoles(roles);
		user.setProfilePic(GlobalConstants.DEFAULT_PROFILE_IMAGE_NAME);
		return this.saveOrUpdateUser(user);
	}

	@Override
	public Optional<AppUser> getUserByIdOptional(int userId) {
		return userRepository.findById(userId);
	}

	@Override
	public AppUser getUserById(int userId) {
		AppUser foundUser = this.getUserByIdOptional(userId).orElseThrow(
				() -> new CustomException(HttpStatus.NOT_FOUND, ExceptionDetailsEnum.USER_NOT_FOUND_WITH_ID, userId));
		return foundUser;
	}

	@Override
	public Optional<AppUser> getUserByUsernameOptional(String username) {
		return userRepository.findUserByUsername(username);
	}

	@Override
	public List<AppUser> getAllUsers() {
		List<AppUser> allusers = userRepository.findAll();
		return allusers;
	}

	@Override
	public AppUser updateUserByUserId(AppUserDto userDto, int userId) {
		AppUser foundUser = this.getUserById(userId);

		if (userDto.getUsername() != null) {
			if (this.getUserByUsernameOptional(userDto.getUsername().toLowerCase()).isPresent())
				throw new CustomException(HttpStatus.CONFLICT, ExceptionDetailsEnum.USERNAME_ALREADY_TAKEN,
						userDto.getUsername().toLowerCase());
		}

		foundUser.setName(userDto.getName() == null ? foundUser.getName() : userDto.getName());
		foundUser.setUsername(userDto.getUsername() == null ? foundUser.getUsername().toLowerCase()
				: userDto.getUsername().toLowerCase());
		foundUser.setPassword(userDto.getPassword() == null ? foundUser.getPassword() : userDto.getPassword());
		foundUser.setAbout(userDto.getAbout() == null ? foundUser.getAbout() : userDto.getAbout());
		AppUser updatedUser = this.saveOrUpdateUser(foundUser);
		return updatedUser;
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
}
