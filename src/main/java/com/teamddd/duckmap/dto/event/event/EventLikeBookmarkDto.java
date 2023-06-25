package com.teamddd.duckmap.dto.event.event;

import com.querydsl.core.annotations.QueryProjection;
import com.teamddd.duckmap.entity.Event;

import lombok.Getter;

@Getter
public class EventLikeBookmarkDto {
	private Event event;
	private Long likeId;
	private Long bookmarkId;

	@QueryProjection
	public EventLikeBookmarkDto(Event event, Long likeId, Long bookmarkId) {
		this.event = event;
		this.likeId = likeId;
		this.bookmarkId = bookmarkId;
	}
}
