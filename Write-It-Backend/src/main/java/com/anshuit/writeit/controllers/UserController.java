package com.anshuit.writeit.controllers;

import java.time.LocalDateTime;
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
import com.anshuit.writeit.dto.AppUserDto;
import com.anshuit.writeit.entities.AppUser;
import com.anshuit.writeit.exceptions.ApiResponse;
import com.anshuit.writeit.exceptions.CustomException;
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
	public ResponseEntity<AppUserDto> getUserByUserId(@PathVariable Integer userId) {
		AppUser user = userService.getUserById(userId);
		AppUserDto userDto = dataTransferService.mapUserToUserDto(user);
		return new ResponseEntity<AppUserDto>(userDto, HttpStatus.OK);
	}

	// SERVE USER IMAGE
	@GetMapping(value = "/images/serve-user-image/{userId}")
	public ResponseEntity<byte[]> serveUserProfileImage(@PathVariable("userId") int userId) {
		AppUser foundUser = userService.getUserById(userId);
		if (foundUser.getProfilePic().equals(GlobalConstants.DEFAULT_PROFILE_IMAGE_NAME)) {
			throw new CustomException("Default Image Is Set , Will Be Taken From Frontend : "
					+ GlobalConstants.DEFAULT_PROFILE_IMAGE_NAME, HttpStatus.OK);
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
	public ResponseEntity<ApiResponse> deleteAllUsers() {
		userService.deleteAllUsers();
		ApiResponse apiResponse = new ApiResponse("All Users Deleted Successfully !!", LocalDateTime.now(),
				HttpStatus.OK, HttpStatus.OK.value());
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	// DELETE SINGLE USER
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<ApiResponse> deleteSingleUser(@PathVariable("userId") int userId) {
		userService.deleteUserByUserId(userId);
		ApiResponse apiResponse = new ApiResponse("User Successfully Deleted With UserId :" + userId,
				LocalDateTime.now(), HttpStatus.OK, HttpStatus.OK.value());
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	// CREATE NEW USER
	@PostMapping("/users")
	public ResponseEntity<AppUserDto> addNewUser(@Valid @RequestBody AppUserDto userDto) {
		AppUser user = dataTransferService.mapUserDtoToUser(userDto);
		AppUser createdUser = userService.createUser(user);
		return new ResponseEntity<AppUserDto>(dataTransferService.mapUserToUserDto(createdUser), HttpStatus.CREATED);
	}

	// GET ALL USERS
	@GetMapping("/users")
	public ResponseEntity<List<AppUserDto>> getAllUsers() {
		List<AppUserDto> allUsers = userService.getAllUsers().stream()
				.map(user -> modelMapper.map(user, AppUserDto.class)).collect(Collectors.toList());
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}
}
