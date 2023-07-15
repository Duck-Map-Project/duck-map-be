package com.teamddd.duckmap.dto.event.event;

import com.querydsl.core.annotations.QueryProjection;
import com.teamddd.duckmap.entity.Event;

import lombok.Getter;

@Getter
public class EventLikeReviewCountDto {
	private Event event;
	private Long likeCount;
	private Long reviewCount;

	@QueryProjection
	public EventLikeReviewCountDto(Event event, Long likeCount, Long reviewCount) {
		this.event = event;
		this.likeCount = likeCount;
		this.reviewCount = reviewCount;
	}
}
