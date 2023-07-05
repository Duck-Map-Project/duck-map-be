package com.teamddd.duckmap.dto.review;

import java.time.LocalDateTime;

import com.teamddd.duckmap.common.ApiUrl;
import com.teamddd.duckmap.entity.Review;
import com.teamddd.duckmap.entity.ReviewImage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventReviewsRes {
	private Long id;
	private String userProfile;
	private String username;
	private LocalDateTime createdAt;
	private int score;
	private String reviewImage;
	private String content;

	public static EventReviewsRes of(Review review) {
		return EventReviewsRes.builder()
			.id(review.getId())
			.userProfile(ApiUrl.IMAGE + review.getMember().getImage())
			.username(review.getMember().getUsername())
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
			.build();
	}
}
