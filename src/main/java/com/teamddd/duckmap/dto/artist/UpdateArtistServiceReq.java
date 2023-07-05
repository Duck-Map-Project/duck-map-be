package com.teamddd.duckmap.dto.artist;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateArtistServiceReq {
	private Long id;
	private Long groupId;
	private String name;
	private String image;
	private Long artistTypeId;
}
