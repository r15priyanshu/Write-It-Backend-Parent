package com.anshuit.writeit.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ApiResponseEnum {

	ALL_USERS_SUCCESSFULLY_DELETED("All Users Successfully Deleted !!", HttpStatus.OK),
	USER_SUCCESSFULLY_DELETED_WITH_ID("User Successfully Deleted With UserId : %s", HttpStatus.OK),
	CATEGORY_SUCCESSFULLY_DELETED_WITH_ID("Category Successfully Deleted with CategoryId : %s", HttpStatus.OK),
	COMMENT_SUCCESSFULLY_DELETED_WITH_ID("Comment Successfully Deleted With CommentId : %s", HttpStatus.OK),
	IMAGE_SUCCESSFULLY_ADDED_TO_POST_WITH_ID("Image Successfully Added To The Post With PostId : %s", HttpStatus.OK),
	POST_SUCCESSFULLY_DELETED_WITH_ID("Post Successfully Deleted With PostId : %s", HttpStatus.OK),;

	private final String message;
	private HttpStatus httpStatus;

	private ApiResponseEnum(String message, HttpStatus httpStatus) {
		this.message = message;
		this.httpStatus = httpStatus;
	}
}
