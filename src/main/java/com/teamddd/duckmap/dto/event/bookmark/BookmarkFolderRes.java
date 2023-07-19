package com.teamddd.duckmap.dto.event.bookmark;

import com.teamddd.duckmap.common.ApiUrl;
import com.teamddd.duckmap.entity.EventBookmarkFolder;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkFolderRes {
	private Long id;
	private String name;
	private String image;
	private String color;

	public static BookmarkFolderRes of(EventBookmarkFolder eventBookmarkFolder) {
		return BookmarkFolderRes.builder()
			.id(eventBookmarkFolder.getId())
			.name(eventBookmarkFolder.getName())
			.image(ApiUrl.IMAGE + eventBookmarkFolder.getImage())
			.color(eventBookmarkFolder.getColor())
			.build();
	}
}
