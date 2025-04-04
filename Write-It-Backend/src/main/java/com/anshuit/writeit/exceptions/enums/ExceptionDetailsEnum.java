package com.anshuit.writeit.exceptions.enums;

import com.anshuit.writeit.constants.GlobalConstants;
import com.anshuit.writeit.utils.GlobalUtils;

public enum ExceptionDetailsEnum {

	// USER RELATED CONSTANTS
	DEFAULT_USER_PROFILE_IMAGE_SET(null, "Default User Profile Image Is Set , Will Be Taken From Frontend : "+ GlobalConstants.DEFAULT_PROFILE_IMAGE_NAME),

	USER_NOT_FOUND_WITH_ID("1001", "User Not Found With UserId : %d"),
	
	USERNAME_ALREADY_TAKEN("1002","Username Already Taken : %s"),
	
	USER_PASSWORD_DID_NOT_MATCH("1003", "Invalid Password !! Password Did Not Match !!"),
	
	// ROLE RELATED CONSTANTS
	ROLE_NOT_FOUND_WITH_ID("1051", "Role Not Found With RoleId : %d"),
	
	ROLE_NOT_FOUND_WITH_ROLE_NAME("1051", "Role Not Found With Role Name : %s"),
	
	// CATEGORY RELATED CONSTANTS
	CATEGORY_NOT_FOUND_WITH_ID("2001", "Category Not Found With CategoryId : %d"),
		
	CATEGORY_NOT_FOUND_WITH_CATEGORY_NAME("2002", "Category Not Found With Category Name : %s"),
	
	// POSTS RELATED CONSTANTS
	DEFAULT_POST_IMAGE_SET(null, "Default Post Image Is Set , Will Be Taken From Frontend : " + GlobalConstants.DEFAULT_POST_IMAGE_NAME),
	
	POST_NOT_FOUND_WITH_ID("3001", "Post Not Found With PostId : %d"),
	
	ERROR_IN_UPLOADING_IMAGE_WITH_POST("3002", "Error In Uploading Image Along With Post !!"),
	
	ERROR_IN_UPLOADING_IMAGE_TO_ALREADY_UPLOADED_POST("3003", "Error In Uploading Image To Post With PostId : %d !!"),;

	private final String exceptionCode;
	private final String exceptionMessage;

	private ExceptionDetailsEnum(String exceptionCode, String exceptionMessage) {
		this.exceptionCode = exceptionCode;
		this.exceptionMessage = exceptionMessage;
	}

	public String getExceptionCode() {
		return exceptionCode;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public static String getFormattedExceptionMessage(ExceptionDetailsEnum exceptionDetailsEnum, Object... args) {
		return GlobalUtils.getFormattedString(exceptionDetailsEnum.getExceptionMessage(), args);
	}
}
