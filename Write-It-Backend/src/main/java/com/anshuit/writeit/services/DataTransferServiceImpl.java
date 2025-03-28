package com.anshuit.writeit.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anshuit.writeit.dto.AppUserDto;
import com.anshuit.writeit.entities.AppUser;

@Service
public class DataTransferServiceImpl {

	@Autowired
	private ModelMapper modelMapper;

	public AppUser mapUserDtoToUser(AppUserDto userDto) {
		return modelMapper.map(userDto, AppUser.class);
	}
	
	public AppUserDto mapUserToUserDto(AppUser user) {
		return modelMapper.map(user, AppUserDto.class);
	}
}
