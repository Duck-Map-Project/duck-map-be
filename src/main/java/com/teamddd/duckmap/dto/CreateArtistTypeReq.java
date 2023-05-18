package com.teamddd.duckmap.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;

@Getter
public class CreateArtistTypeReq {
	private Long id;
	@NotEmpty
	private String type;
}
