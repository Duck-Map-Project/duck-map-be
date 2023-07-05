package com.teamddd.duckmap.dto.review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.teamddd.duckmap.common.ApiUrl;
import com.teamddd.duckmap.entity.Review;
import com.teamddd.duckmap.entity.ReviewImage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewRes {
	private Long id;
	private String userProfile;
	private String username;
	private LocalDateTime createdAt;
	private int score;
	private String content;
	private List<String> photos;

	public static ReviewRes of(Review review) {
		List<String> imageResList = review.getReviewImages().stream()
			.map(ReviewImage::getImage)
			.map(image -> ApiUrl.IMAGE + image)
			.collect(Collectors.toList());

		return ReviewRes.builder()
			.id(review.getId())
			.userProfile(ApiUrl.IMAGE + review.getMember().getImage())
			.username(review.getMember().getUsername())
			.createdAt(review.getCreatedAt())
			.score(review.getScore())
			.content(review.getContent())
			.photos(imageResList)
			.build();
	}
}
