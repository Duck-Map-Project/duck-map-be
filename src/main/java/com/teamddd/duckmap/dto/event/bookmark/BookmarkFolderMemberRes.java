package com.teamddd.duckmap.dto.event.bookmark;

import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.entity.EventBookmarkFolder;

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

	public static BookmarkFolderMemberRes of(EventBookmarkFolder bookmarkFolder) {
		return BookmarkFolderMemberRes.builder()
			.id(bookmarkFolder.getId())
			.name(bookmarkFolder.getName())
			.image(
				ImageRes.builder()
					.filename(bookmarkFolder.getImage())
					.build()
			)
			.memberId(bookmarkFolder.getMember().getId())
			.username(bookmarkFolder.getMember().getUsername())
			.build();
	}
}
