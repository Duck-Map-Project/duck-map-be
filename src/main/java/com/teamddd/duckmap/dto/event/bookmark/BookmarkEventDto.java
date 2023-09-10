package com.teamddd.duckmap.dto.event.bookmark;

import com.querydsl.core.annotations.QueryProjection;

public record BookmarkEventDto(Long eventId, String eventStoreName, String eventThumbnail, Long eventBookmarkId) {
	@QueryProjection
	public BookmarkEventDto {
	}
}
