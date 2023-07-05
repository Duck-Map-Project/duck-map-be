package com.teamddd.duckmap.dto.review;

import java.time.LocalDate;

import com.teamddd.duckmap.common.ApiUrl;
import com.teamddd.duckmap.entity.Review;
import com.teamddd.duckmap.entity.ReviewImage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewsRes {
	private Long id;
	private boolean inProgress;
	private String image;

	public static ReviewsRes of(Review review, LocalDate date) {
		return ReviewsRes.builder()
			.id(review.getId())
			.image(
				review.getReviewImages().stream()
					.map(ReviewImage::getImage)
					.findFirst()
					.map(image -> ApiUrl.IMAGE + image)
					.orElse(null)
			)
			.inProgress(review.getEvent().isInProgress(date))
			.build();
	}

}
