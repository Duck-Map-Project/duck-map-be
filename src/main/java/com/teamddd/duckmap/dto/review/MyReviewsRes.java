package com.teamddd.duckmap.dto.review;

import java.time.LocalDateTime;

import com.teamddd.duckmap.dto.ImageRes;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyReviewsRes {
	private Long id;
	private Long eventId;
	private ImageRes eventImage;
	private String eventStoreName;
	private LocalDateTime createdAt;
	private int score;
	private ImageRes reviewImage;
	private String content;
}
