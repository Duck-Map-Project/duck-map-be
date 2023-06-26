package com.teamddd.duckmap.dto.event.bookmark;

import com.querydsl.core.annotations.QueryProjection;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventBookmark;

import lombok.Getter;

@Getter
public class BookmarkEventDto {
	private final Event event;
	private final EventBookmark eventBookmark;

	@QueryProjection
	public BookmarkEventDto(Event event, EventBookmark eventBookmark) {
		this.event = event;
		this.eventBookmark = eventBookmark;
	}
}
