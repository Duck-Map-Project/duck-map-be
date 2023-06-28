package com.teamddd.duckmap.dto.event.bookmark;

import java.util.List;

import com.querydsl.core.annotations.QueryProjection;
import com.teamddd.duckmap.entity.EventImage;

import lombok.Getter;

@Getter
public class BookmarkEventDto {
	private final Long eventId;
	private final String eventStoreName;
	private final Long eventBookmarkId;
	private final List<EventImage> eventImageList;

	@QueryProjection
	public BookmarkEventDto(Long eventId, String eventStoreName, List<EventImage> eventImageList,
		Long eventBookmarkId) {
		this.eventId = eventId;
		this.eventStoreName = eventStoreName;
		this.eventImageList = eventImageList;
		this.eventBookmarkId = eventBookmarkId;
	}
}
