package com.teamddd.duckmap.dto.event.bookmark;

import com.teamddd.duckmap.common.ApiUrl;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkedEventRes {
	private Long id;
	private Long eventId;
	private String storeName;
	private String image;

	public static BookmarkedEventRes of(BookmarkEventDto bookmarkEventDto) {
		return BookmarkedEventRes.builder()
			.id(bookmarkEventDto.getEventBookmarkId())
			.eventId(bookmarkEventDto.getEventId())
			.storeName(bookmarkEventDto.getEventStoreName())
			.image(ApiUrl.IMAGE + bookmarkEventDto.getEventThumbnail())
			.build();
	}
}
