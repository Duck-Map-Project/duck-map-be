package com.teamddd.duckmap.dto.review;

import com.querydsl.core.annotations.QueryProjection;
import com.teamddd.duckmap.entity.Review;

import lombok.Getter;

@Getter
public class ReviewEventDto {
	private Review review;
	private Long eventId;
	private String eventStoreName;

	@QueryProjection
	public ReviewEventDto(Review review, Long eventId, String eventStoreName) {
		this.review = review;
		this.eventId = eventId;
		this.eventStoreName = eventStoreName;
	}
}
