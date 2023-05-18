package com.teamddd.duckmap.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;

@Getter
public class ArtistTypeReq {
	private Long id;
	@NotEmpty
	private String type;
}
