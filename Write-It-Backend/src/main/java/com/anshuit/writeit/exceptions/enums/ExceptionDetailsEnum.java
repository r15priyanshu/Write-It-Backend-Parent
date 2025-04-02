package com.anshuit.writeit.exceptions.enums;

import com.anshuit.writeit.constants.GlobalConstants;
import com.anshuit.writeit.utils.GlobalUtils;

public enum ExceptionDetailsEnum {

	// User Related Constants
	DEFAULT_USER_PROFILE_IMAGE_SET("","Default User Profile Image Is Set , Will Be Taken From Frontend : "+ GlobalConstants.DEFAULT_PROFILE_IMAGE_NAME),

	USER_NOT_FOUND_WITH_ID("1001", "User not found with id : %s"),

	USER_NOT_FOUND_WITH_EMAIL("1002", "User not found with email : %s"),

	USER_ALREADY_EXIST_WITH_EMAIL("1003", "User already exist with email : %s"),

	USER_PASSWORD_DID_NOT_MATCH("1004", "Invalid Password !! Password did not match !!"),

	// Role Related Constants
	ROLE_NOT_FOUND_WITH_ID("1051", "Role not found with roleId : %s");

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
