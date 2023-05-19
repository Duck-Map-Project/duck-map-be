package com.teamddd.duckmap.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class CreateArtistReq {
	private Long groupId;
	@NotBlank
	private String name;
	@NotNull
	private String image;
	@NotNull
	private Long artistTypeId;
}
