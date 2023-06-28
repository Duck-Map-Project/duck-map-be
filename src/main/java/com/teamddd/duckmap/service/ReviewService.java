package com.teamddd.duckmap.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.review.CreateReviewReq;
import com.teamddd.duckmap.dto.review.MyReviewServiceReq;
import com.teamddd.duckmap.dto.review.MyReviewsRes;
import com.teamddd.duckmap.dto.review.ReviewEventDto;
import com.teamddd.duckmap.dto.review.ReviewRes;
import com.teamddd.duckmap.dto.review.ReviewSearchServiceReq;
import com.teamddd.duckmap.dto.review.ReviewsRes;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.entity.Review;
import com.teamddd.duckmap.entity.ReviewImage;
import com.teamddd.duckmap.exception.NonExistentReviewException;
import com.teamddd.duckmap.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

	private final EventService eventService;
	private final ArtistService artistService;
	private final ReviewRepository reviewRepository;

	@Transactional
	public Long createReview(CreateReviewReq createReviewReq, Member member) {
		Event event = eventService.getEvent(createReviewReq.getEventId());

		Review review = Review.builder()
			.member(member)
			.event(event)
			.content(createReviewReq.getContent())
			.score(createReviewReq.getScore())
			.build();

		List<String> imageFilenames = createReviewReq.getImageFilenames();
		for (String imageFilename : imageFilenames) {
			review.getReviewImages().add(
				ReviewImage.builder()
					.review(review)
					.image(imageFilename)
					.build()
			);
		}

		reviewRepository.save(review);

		return review.getId();
	}

	public Page<ReviewsRes> getReviewsResList(ReviewSearchServiceReq request) {
		if (request.getArtistId() != null) {
			artistService.getArtist(request.getArtistId());
		}
		LocalDate searchDate = request.isOnlyInProgress() ? request.getDate() : null;
		Page<Review> reviews = reviewRepository.findByArtistAndDate(request.getArtistId(),
			searchDate, request.getPageable());

		return reviews.map(Review -> ReviewsRes.of(Review, request.getDate()));
	}

	//Review 단건 조회
	public Review getReview(Long reviewId) throws NonExistentReviewException {
		return reviewRepository.findById(reviewId)
			.orElseThrow(NonExistentReviewException::new);
	}

	public ReviewRes getReviewRes(Long reviewId) throws NonExistentReviewException {
		return reviewRepository.findById(reviewId)
			.map(ReviewRes::of)
			.orElseThrow(NonExistentReviewException::new);
	}

	public Page<ReviewsRes> getReviewsResPage(Pageable pageable) {
		Page<Review> reviewsPage = reviewRepository.findAll(pageable);
		LocalDate now = LocalDate.now();
		return reviewsPage.map(Review -> ReviewsRes.of(Review, now));
	}

	public Page<MyReviewsRes> getMyReviewsRes(MyReviewServiceReq request) {
		Page<ReviewEventDto> myEvents = reviewRepository.findWithEventByMemberId(request.getMemberId(),
			request.getPageable());
		return myEvents.map(MyReviewsRes::of);
	}

}
