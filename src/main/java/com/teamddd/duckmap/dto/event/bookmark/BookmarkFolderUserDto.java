package com.teamddd.duckmap.dto.event.bookmark;

import com.querydsl.core.annotations.QueryProjection;
import com.teamddd.duckmap.entity.EventBookmarkFolder;

import lombok.Getter;

@Getter
public class BookmarkFolderUserDto {
	private final EventBookmarkFolder bookmarkFolder;
	private final Long userId;
	private final String username;

	@QueryProjection
	public BookmarkFolderUserDto(EventBookmarkFolder bookmarkFolder, Long userId, String username) {
		this.bookmarkFolder = bookmarkFolder;
		this.userId = userId;
		this.username = username;
	}
}
