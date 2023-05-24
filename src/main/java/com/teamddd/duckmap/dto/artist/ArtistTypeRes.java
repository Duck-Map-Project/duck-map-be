package com.teamddd.duckmap.dto.artist;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtistTypeRes {
	private Long id;
	private String type;
}
