package com.teamddd.duckmap.dto.event.event;

import com.querydsl.core.annotations.QueryProjection;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventBookmark;
import com.teamddd.duckmap.entity.EventLike;

import lombok.Getter;

@Getter
public class EventLikeBookmarkDto {
	private Event event;
	private EventLike like;
	private EventBookmark bookmark;

	@QueryProjection
	public EventLikeBookmarkDto(Event event, EventLike like, EventBookmark bookmark) {
		this.event = event;
		this.like = like;
		this.bookmark = bookmark;
	}
}
