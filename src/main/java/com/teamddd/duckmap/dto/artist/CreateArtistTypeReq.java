package com.teamddd.duckmap.dto.artist;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class CreateArtistTypeReq {
	@NotBlank
	private String type;
}
