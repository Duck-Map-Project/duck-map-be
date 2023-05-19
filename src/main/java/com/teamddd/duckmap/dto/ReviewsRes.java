package com.teamddd.duckmap.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewsRes {
	private Long id;
	private boolean inProgress;
	private ImageRes image;
}
