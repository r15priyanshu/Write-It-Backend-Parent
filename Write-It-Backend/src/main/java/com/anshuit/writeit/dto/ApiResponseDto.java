package com.anshuit.writeit.dto;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.anshuit.writeit.enums.ApiResponseEnum;
import com.anshuit.writeit.utils.GlobalUtils;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseDto {
	private LocalDateTime timestamp;
	private int statusCode;
	private String message;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String exceptionCode;

	private HttpStatus status;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String path;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Map<Object, Object> data;

	public static ApiResponseDto generateApiResponse(ApiResponseEnum apiResponse, Object... args) {
		ApiResponseDto apiResponseDto = ApiResponseDto
				.builder()
				.message(GlobalUtils.getFormattedString(apiResponse.getMessage(), args))
				.timestamp(LocalDateTime.now())
				.status(apiResponse.getHttpStatus())
				.statusCode(apiResponse.getHttpStatus().value())
				.exceptionCode(null)
				.path(null)
				.build();
		return apiResponseDto;
	}
}
