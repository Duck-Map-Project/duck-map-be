package com.teamddd.duckmap.dto.event.bookmark;

import org.springframework.data.domain.Page;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkFolderMemberRes {
	private Long id;
	private String name;
	private Long memberId;
	private String username;
	private Page<BookmarkedEventRes> bookmarkedEventResPage;
}
