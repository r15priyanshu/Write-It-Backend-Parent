package com.anshuit.writeit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
	private int categoryId;
	private String categoryName;
	private String categoryDescription;
}
