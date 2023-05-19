package com.teamddd.duckmap.dto.event.category;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class UpdateEventCategoryReq {
	@NotBlank
	private String category;
}
