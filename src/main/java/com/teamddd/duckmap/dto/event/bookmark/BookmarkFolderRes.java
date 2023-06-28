package com.teamddd.duckmap.dto.event.bookmark;

import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.entity.EventBookmarkFolder;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkFolderRes {
	private Long id;
	private String name;
	private ImageRes image;

	public static BookmarkFolderRes of(EventBookmarkFolder eventBookmarkFolder) {
		return BookmarkFolderRes.builder()
			.id(eventBookmarkFolder.getId())
			.name(eventBookmarkFolder.getName())
			.image(
				ImageRes.builder()
					.filename(eventBookmarkFolder.getImage())
					.build()
			)
			.build();
	}
}
