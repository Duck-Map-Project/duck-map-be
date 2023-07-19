package com.teamddd.duckmap.dto.event.bookmark;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkFolderMemberRes {
	private Long id;
	private String name;
	private String image;
	private String color;
	private Long memberId;
	private String username;
}
