package com.anshuit.writeit.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.tika.Tika;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.anshuit.writeit.constants.GlobalConstants;
import com.anshuit.writeit.dto.ApiResponseDto;
import com.anshuit.writeit.dto.AppUserDto;
import com.anshuit.writeit.entities.AppUser;
import com.anshuit.writeit.enums.ApiResponseEnum;
import com.anshuit.writeit.exceptions.CustomException;
import com.anshuit.writeit.exceptions.enums.ExceptionDetailsEnum;
import com.anshuit.writeit.services.DataTransferServiceImpl;
import com.anshuit.writeit.services.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private DataTransferServiceImpl dataTransferService;

	// GET SINGLE USER
	@GetMapping("/users/{userId}")
	public ResponseEntity<AppUserDto> getUserByUserId(@PathVariable("userId") int userId) {
		AppUser user = userService.getUserById(userId);
		AppUserDto userDto = dataTransferService.mapUserToUserDto(user);
		return new ResponseEntity<AppUserDto>(userDto, HttpStatus.OK);
	}

	// SERVE USER IMAGE
	@GetMapping(value = "/images/serve-user-image/{userId}")
	public ResponseEntity<byte[]> serveUserProfileImage(@PathVariable("userId") int userId) {
		AppUser foundUser = userService.getUserById(userId);
		if (foundUser.getProfilePic().equals(GlobalConstants.DEFAULT_PROFILE_IMAGE_NAME)) {
			throw new CustomException(HttpStatus.OK, ExceptionDetailsEnum.DEFAULT_USER_PROFILE_IMAGE_SET);
		}
		// Detect MIME type of image data
		String contentType = new Tika().detect(foundUser.getImageData());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(contentType));
		return new ResponseEntity<>(foundUser.getImageData(), headers, HttpStatus.OK);
	}

	// UPDATE SINGLE USER
	@PutMapping("/users/{userId}")
	public ResponseEntity<AppUserDto> updateUserByUserId(@Valid @RequestBody AppUserDto userDto,
			@PathVariable("userId") int userId) {
		AppUser updatedUser = userService.updateUserByUserId(userDto, userId);
		AppUserDto updatedUserDto = dataTransferService.mapUserToUserDto(updatedUser);
		return new ResponseEntity<AppUserDto>(updatedUserDto, HttpStatus.OK);
	}

	// DELETE ALL USERS
	@DeleteMapping("/users")
	public ResponseEntity<ApiResponseDto> deleteAllUsers() {
		userService.deleteAllUsers();
		ApiResponseDto apiResponse = ApiResponseDto.generateApiResponse(ApiResponseEnum.ALL_USERS_SUCCESSFULLY_DELETED);
		return new ResponseEntity<>(apiResponse, ApiResponseEnum.ALL_USERS_SUCCESSFULLY_DELETED.getHttpStatus());
	}

	// DELETE SINGLE USER
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<ApiResponseDto> deleteSingleUser(@PathVariable("userId") int userId) {
		userService.deleteUserByUserId(userId);
		ApiResponseDto apiResponse = ApiResponseDto
				.generateApiResponse(ApiResponseEnum.USER_SUCCESSFULLY_DELETED_WITH_ID, userId);
		return new ResponseEntity<>(apiResponse, ApiResponseEnum.USER_SUCCESSFULLY_DELETED_WITH_ID.getHttpStatus());
	}

	// CREATE NEW USER
	@PostMapping("/users")
	public ResponseEntity<AppUserDto> addNewUser(@Valid @RequestBody AppUserDto userDto) {
		AppUser user = dataTransferService.mapUserDtoToUser(userDto);
		AppUser createdUser = userService.createUser(user);
		AppUserDto createdUserDto = dataTransferService.mapUserToUserDto(createdUser);
		return new ResponseEntity<AppUserDto>(createdUserDto, HttpStatus.CREATED);
	}

	// GET ALL USERS
	@GetMapping("/users")
	public ResponseEntity<List<AppUserDto>> getAllUsers() {
		List<AppUserDto> allUsers = userService.getAllUsers().stream()
				.map(user -> modelMapper.map(user, AppUserDto.class)).collect(Collectors.toList());
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}
}
