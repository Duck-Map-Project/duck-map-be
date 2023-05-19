package com.teamddd.duckmap.dto.event.category;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class CreateEventCategoryReq {
	@NotBlank
	private String category;
}
