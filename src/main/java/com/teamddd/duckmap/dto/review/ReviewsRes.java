package com.teamddd.duckmap.dto.review;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.teamddd.duckmap.common.ApiUrl;
import com.teamddd.duckmap.dto.artist.ArtistRes;
import com.teamddd.duckmap.dto.event.category.EventCategoryRes;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventArtist;
import com.teamddd.duckmap.entity.EventInfoCategory;
import com.teamddd.duckmap.entity.Review;
import com.teamddd.duckmap.entity.ReviewImage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewsRes {
	private Long id;
	private boolean inProgress;
	private int score;
	private String image;
	private List<ArtistRes> artists;
	private List<EventCategoryRes> categories;

	public static ReviewsRes of(Review review, LocalDate date) {
		Event event = review.getEvent();
		return ReviewsRes.builder()
			.id(review.getId())
			.image(
				review.getReviewImages().stream()
					.map(ReviewImage::getImage)
					.findFirst()
					.map(image -> ApiUrl.IMAGE + image)
					.orElse(null)
			)
			.score(review.getScore())
			.inProgress(event.isInProgress(date))
			.artists(
				event.getEventArtists().stream()
					.map(EventArtist::getArtist)
					.filter(Objects::nonNull)
					.map(ArtistRes::of)
					.collect(Collectors.toList())
			)
			.categories(
				event.getEventInfoCategories().stream()
					.map(EventInfoCategory::getEventCategory)
					.map(EventCategoryRes::of)
					.collect(Collectors.toList())
			)
			.build();
	}

}
