package com.anshuit.writeit.controllers;

import org.modelmapper.ModelMapper;
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

@RestController
public class AuthenticationController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ModelMapper modeMapper;

	@PostMapping("/api/login")
	public ResponseEntity<AppUserDto> performLogin(@RequestBody AppUser user) {
		AppUser founduser = userRepository.findUserByUsernameAndPassword(user.getUsername(), user.getPassword())
				.orElseThrow(() -> new CustomException("Invalid Credentials !!", HttpStatus.UNAUTHORIZED));
		return new ResponseEntity<AppUserDto>(modeMapper.map(founduser, AppUserDto.class), HttpStatus.OK);
	}
}
