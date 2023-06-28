package com.teamddd.duckmap.dto.review;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EventReviewServiceReq {
	private Long eventId;
	private Pageable pageable;
}
