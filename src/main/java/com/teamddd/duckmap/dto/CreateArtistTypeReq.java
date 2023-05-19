package com.teamddd.duckmap.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class CreateArtistTypeReq {
	@NotBlank
	private String type;
}
