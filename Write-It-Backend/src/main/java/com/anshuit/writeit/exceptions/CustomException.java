package com.anshuit.writeit.exceptions;

import org.springframework.http.HttpStatus;

import com.anshuit.writeit.exceptions.enums.ExceptionDetailsEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private HttpStatus status;
	private ExceptionDetailsEnum exceptionDetailsEnum;

	public CustomException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}

	public CustomException(HttpStatus status, ExceptionDetailsEnum exceptionDetailsEnum, Object... args) {
		super(ExceptionDetailsEnum.getFormattedExceptionMessage(exceptionDetailsEnum, args));
		this.exceptionDetailsEnum = exceptionDetailsEnum;
		this.status = status;
	}
}
