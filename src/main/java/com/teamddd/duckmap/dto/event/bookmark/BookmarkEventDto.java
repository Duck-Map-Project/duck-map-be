package com.teamddd.duckmap.dto.event.bookmark;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class BookmarkEventDto {
	private final Long eventId;
	private final String eventStoreName;
	private final String eventThumbnail;
	private final Long eventBookmarkId;

	@QueryProjection
	public BookmarkEventDto(Long eventId, String eventStoreName, String eventThumbnail,
		Long eventBookmarkId) {
		this.eventId = eventId;
		this.eventStoreName = eventStoreName;
		this.eventThumbnail = eventThumbnail;
		this.eventBookmarkId = eventBookmarkId;
	}
}
