package com.teamddd.duckmap.dto.review;

import com.teamddd.duckmap.dto.ImageRes;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewsRes {
	private Long id;
	private boolean inProgress;
	private ImageRes image;
}
