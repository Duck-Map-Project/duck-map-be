package com.teamddd.duckmap.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkFolderRes {
	private Long id;
	private String name;
	private ImageRes image;
	private List<BookmarkRes> bookmarks;
}
