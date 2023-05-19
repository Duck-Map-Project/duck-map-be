package com.teamddd.duckmap.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtistTypeRes {
	private Long id;
	private String type;
}
