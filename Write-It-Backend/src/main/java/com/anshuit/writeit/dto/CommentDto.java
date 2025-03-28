package com.anshuit.writeit.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
	private Integer commentId;
	private String comment;
	private Date commentdate;
	private AppUserDto user;
}
