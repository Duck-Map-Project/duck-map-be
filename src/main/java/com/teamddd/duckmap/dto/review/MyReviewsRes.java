package com.teamddd.duckmap.dto.review;

import java.time.LocalDateTime;

import com.teamddd.duckmap.common.ApiUrl;
import com.teamddd.duckmap.entity.Review;
import com.teamddd.duckmap.entity.ReviewImage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyReviewsRes {
	private Long id;
	private Long eventId;
	private String eventStoreName;
	private LocalDateTime createdAt;
	private int score;
	private String reviewImage;
	private String content;
	private boolean isBlind;

	public static MyReviewsRes of(ReviewEventDto reviewEventDto) {
		Review review = reviewEventDto.getReview();
		Long eventId = reviewEventDto.getEventId();
		String eventStoreName = reviewEventDto.getEventStoreName();
		return MyReviewsRes.builder()
			.id(review.getId())
			.eventId(eventId)
			.eventStoreName(eventStoreName)
			.createdAt(review.getCreatedAt())
			.score(review.getScore())
			.reviewImage(
				review.getReviewImages().stream()
					.map(ReviewImage::getImage)
					.findFirst()
					.map(image -> ApiUrl.IMAGE + image)
					.orElse(null)
			)
			.content(review.getContent())
			.isBlind(false)
			.build();
	}
}
