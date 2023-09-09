package com.teamddd.duckmap.dto.artist;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class CreateArtistReq {
	@NotNull
	private Long artistTypeId;
	private Long groupId;
	@NotBlank
	private String name;
	@NotBlank
	private String image;
}
