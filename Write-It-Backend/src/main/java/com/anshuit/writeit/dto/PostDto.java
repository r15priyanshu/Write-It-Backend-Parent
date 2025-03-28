package com.anshuit.writeit.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class PostDto {
	private int postId;
	private String title;
	private String content;
	private String image;
	private Date createdDate;
	private byte[] imageData;
	private CategoryDto category;
	private AppUserDto user;
	private List<CommentDto> comments;
}
