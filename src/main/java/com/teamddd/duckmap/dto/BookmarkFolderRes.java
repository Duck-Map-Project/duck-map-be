package com.teamddd.duckmap.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkFolderRes {
	private Long id;
	private String name;
	private ImageRes image;
}
