package com.teamddd.duckmap.dto.review;

import java.time.LocalDateTime;

import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.entity.Review;
import com.teamddd.duckmap.entity.ReviewImage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventReviewsRes {
	private Long id;
	private ImageRes userProfile;
	private String username;
	private LocalDateTime createdAt;
	private int score;
	private ImageRes reviewImage;
	private String content;

	public static EventReviewsRes of(Review review) {
		return EventReviewsRes.builder()
			.id(review.getId())
			.userProfile(
				ImageRes.builder()
					.filename(review.getMember().getImage())
					.build()
			)
			.username(review.getMember().getUsername())
			.createdAt(review.getCreatedAt())
			.score(review.getScore())
			.reviewImage(
				ImageRes.builder()
					.filename(
						review.getReviewImages().stream()
							.map(ReviewImage::getImage)
							.findFirst()
							.orElse(null)
					).build()
			)
			.content(review.getContent())
			.build();
	}
}
