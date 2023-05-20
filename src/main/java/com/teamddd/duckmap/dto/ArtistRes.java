package com.teamddd.duckmap.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ArtistRes {
	private Long id;
	private Long groupId;
	private String groupName;
	private String name;
	private ImageRes image;
	private ArtistTypeRes artistType;
}
