package com.teamddd.duckmap.dto.review;

import java.time.LocalDate;

import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.entity.Review;
import com.teamddd.duckmap.entity.ReviewImage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewsRes {
	private Long id;
	private boolean inProgress;
	private ImageRes image;

	public static ReviewsRes of(Review review) {
		LocalDate now = LocalDate.now();
		return ReviewsRes.builder()
			.id(review.getId())
			.image(
				ImageRes.builder()
					.filename(
						review.getReviewImages().stream()
							.map(ReviewImage::getImage)
							.findFirst()
							.orElse(null)
					).build()
			)
			.inProgress(review.getEvent().isInProgress(now))
			.build();
	}

}
