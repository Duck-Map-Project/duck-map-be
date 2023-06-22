package com.teamddd.duckmap.dto.review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.entity.Review;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewRes {
	private Long id;
	private ImageRes userProfile;
	private String username;
	private LocalDateTime createdAt;
	private int score;
	private String content;
	private List<ImageRes> photos;

	public static ReviewRes of(Review review) {
		List<ImageRes> imageResList = review.getReviewImages().stream()
			.map(reviewImage -> ImageRes.builder()
				.filename(reviewImage.getImage())
				.build())
			.collect(Collectors.toList());

		return ReviewRes.builder()
			.id(review.getId())
			.userProfile(
				ImageRes.builder()
					.filename(review.getMember().getImage())
					.build()
			)
			.username(review.getMember().getUsername())
			.createdAt(review.getCreatedAt())
			.score(review.getScore())
			.content(review.getContent())
			.photos(imageResList)
			.build();
	}
}
