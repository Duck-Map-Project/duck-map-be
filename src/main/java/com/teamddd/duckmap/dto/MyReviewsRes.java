package com.teamddd.duckmap.dto;

import java.time.LocalDateTime;

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
