package com.teamddd.duckmap.dto.event.bookmark;

import com.teamddd.duckmap.dto.ImageRes;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkFolderMemberRes {
	private Long id;
	private String name;
	private ImageRes image;
	private Long memberId;
	private String username;
}
