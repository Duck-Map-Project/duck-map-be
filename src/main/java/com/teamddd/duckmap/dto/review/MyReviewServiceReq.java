package com.teamddd.duckmap.dto.review;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MyReviewServiceReq {
	private Long memberId;
	private Pageable pageable;
}
