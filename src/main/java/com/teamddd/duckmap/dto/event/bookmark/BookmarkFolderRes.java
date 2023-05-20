package com.teamddd.duckmap.dto.event.bookmark;

import com.teamddd.duckmap.dto.ImageRes;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkFolderRes {
	private Long id;
	private String name;
	private ImageRes image;
}
