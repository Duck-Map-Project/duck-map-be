package com.teamddd.duckmap.dto.artist;

import com.teamddd.duckmap.entity.ArtistType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtistTypeRes {
	private Long id;
	private String type;

	public static ArtistTypeRes of(ArtistType artistType) {
		return ArtistTypeRes.builder()
			.id(artistType.getId())
			.type(artistType.getType())
			.build();
	}
}
