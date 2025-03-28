package com.anshuit.writeit.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserDto {
	private int userId;

	@NotEmpty(message = "Name Cannot Be Empty or Null.")
	@Size(min = 3, message = "Name Must Be Of Minimum Length 3.")
	private String name;

	@NotEmpty(message = "Username Cannot Be Empty or Null.")
	@Size(min = 5, message = "Username Must Be Of Minimum Length 5.")
	private String username;

	@NotEmpty(message = "Password Cannot Be Empty or Null.")
	@Size(min = 5, message = "Password Must Be Of Minimum Length 5.")

	private String password;
	private String about;
	private String profilePic;
	private String imageData;
	private List<RoleDto> roles;
}
