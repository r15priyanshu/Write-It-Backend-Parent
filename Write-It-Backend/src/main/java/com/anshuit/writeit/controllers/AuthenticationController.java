package com.anshuit.writeit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.anshuit.writeit.dto.AppUserDto;
import com.anshuit.writeit.entities.AppUser;
import com.anshuit.writeit.exceptions.CustomException;
import com.anshuit.writeit.repositories.UserRepository;
import com.anshuit.writeit.services.impls.DataTransferServiceImpl;

@RestController
public class AuthenticationController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DataTransferServiceImpl dataTransferService;

	@PostMapping("/auth/login")
	public ResponseEntity<AppUserDto> performLogin(@RequestBody AppUser user) {
		AppUser founduser = userRepository.findUserByUsernameAndPassword(user.getUsername(), user.getPassword())
				.orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "Invalid Credentials !!"));
		AppUserDto userDto = dataTransferService.mapUserToUserDto(founduser);
		return new ResponseEntity<AppUserDto>(userDto, HttpStatus.OK);
	}
}
