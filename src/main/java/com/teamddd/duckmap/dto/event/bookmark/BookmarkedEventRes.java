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

	public BookmarkedEventRes(Long id, Long eventId, String storeName, String image) {
		this.id = id;
		this.eventId = eventId;
		this.storeName = storeName;
		this.image = image;
	}

	public static BookmarkedEventRes of(BookmarkEventDto bookmarkEventDto) {
		return new BookmarkedEventRes(
			bookmarkEventDto.eventBookmarkId(),
			bookmarkEventDto.eventId(),
			bookmarkEventDto.eventStoreName(),
			ApiUrl.IMAGE + bookmarkEventDto.eventThumbnail()
		);
	}
}

