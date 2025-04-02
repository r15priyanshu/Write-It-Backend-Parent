package com.anshuit.writeit.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.anshuit.writeit.dto.ApiResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiResponseDto> handleCustomException(CustomException ex, HttpServletRequest request) {
		ApiResponseDto apiResponseDto = ApiResponseDto
				.builder()
				.message(ex.getMessage())
				.timestamp(LocalDateTime.now())
				.status(ex.getStatus())
				.statusCode(ex.getStatus().value())
				.exceptionCode(ex.getExceptionDetailsEnum() == null ? "" : ex.getExceptionDetailsEnum().getExceptionCode())
				.path(request.getRequestURI())
				.build();

		return new ResponseEntity<ApiResponseDto>(apiResponseDto, ex.getStatus());
	}

	// Related to Spring data jpa @Valid Annotation
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public HashMap<Object, Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
			HttpServletResponse response) {
		HashMap<Object, Object> hm = new HashMap<>();
		hm.put("status", HttpStatus.BAD_REQUEST);
		hm.put("statuscode", HttpStatus.BAD_REQUEST.value());

		HashMap<Object, Object> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors()
				.forEach((e) -> errors.put(((FieldError) e).getField(), e.getDefaultMessage()));
		hm.put("errors", errors);
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		return hm;
	}
}
