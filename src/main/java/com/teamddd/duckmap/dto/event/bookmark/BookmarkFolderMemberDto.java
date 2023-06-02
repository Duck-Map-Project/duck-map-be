package com.teamddd.duckmap.dto.event.bookmark;

import com.querydsl.core.annotations.QueryProjection;
import com.teamddd.duckmap.entity.EventBookmarkFolder;

import lombok.Getter;

@Getter
public class BookmarkFolderMemberDto {
	private final EventBookmarkFolder bookmarkFolder;
	private final Long memberId;
	private final String username;

	@QueryProjection
	public BookmarkFolderMemberDto(EventBookmarkFolder bookmarkFolder, Long memberId, String username) {
		this.bookmarkFolder = bookmarkFolder;
		this.memberId = memberId;
		this.username = username;
	}
}
