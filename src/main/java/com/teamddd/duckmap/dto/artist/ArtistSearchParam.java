package com.teamddd.duckmap.dto.artist;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtistSearchParam {
	private Long artistTypeId;
	private String artistName;
}
