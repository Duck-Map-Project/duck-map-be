package com.teamddd.duckmap.controller;

import java.time.LocalDate;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.dto.PageReq;
import com.teamddd.duckmap.dto.review.CreateReviewReq;
import com.teamddd.duckmap.dto.review.CreateReviewRes;
import com.teamddd.duckmap.dto.review.EventReviewServiceReq;
import com.teamddd.duckmap.dto.review.EventReviewsRes;
import com.teamddd.duckmap.dto.review.MyReviewServiceReq;
import com.teamddd.duckmap.dto.review.MyReviewsRes;
import com.teamddd.duckmap.dto.review.ReviewRes;
import com.teamddd.duckmap.dto.review.ReviewSearchParam;
import com.teamddd.duckmap.dto.review.ReviewSearchServiceReq;
import com.teamddd.duckmap.dto.review.ReviewsRes;
import com.teamddd.duckmap.dto.review.UpdateReviewReq;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.service.ReviewService;
import com.teamddd.duckmap.util.MemberUtils;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
	private final ReviewService reviewService;

	@Operation(summary = "리뷰 등록")
	@PostMapping
	public CreateReviewRes createReview(@Validated @RequestBody CreateReviewReq createReviewReq) {
		Member member = MemberUtils.getAuthMember().getUser();
		Long reviewId = reviewService.createReview(createReviewReq, member);
		return CreateReviewRes.builder()
			.id(reviewId)
			.build();
	}

	@Operation(summary = "리뷰 상세 조회")
	@GetMapping("/{id}")
	public ReviewRes getReview(@PathVariable Long id) {
		return reviewService.getReviewRes(id);
	}

	@Operation(summary = "리뷰 수정")
	@PutMapping("/{id}")
	public void updateReview(@PathVariable Long id, @Validated @RequestBody UpdateReviewReq updateReviewReq) {
		reviewService.updateReview(id, updateReviewReq);
	}

	@Operation(summary = "리뷰 삭제")
	@DeleteMapping("/{id}")
	public void deleteReview(@PathVariable Long id) {
		reviewService.deleteReview(id);
	}

	@Operation(summary = "리뷰 목록 조회", description = "artist, 날짜 기준 리뷰 목록 조회 기능 구현")
	@GetMapping
	public Page<ReviewsRes> getReviews(PageReq pageReq, @ModelAttribute ReviewSearchParam reviewSearchParam) {

		LocalDate today = LocalDate.now();

		Pageable pageable = PageRequest.of(pageReq.getPageNumber(), pageReq.getPageSize());

		ReviewSearchServiceReq request = ReviewSearchServiceReq.builder()
			.date(today)
			.artistId(reviewSearchParam.getArtistId())
			.onlyInProgress(BooleanUtils.isTrue(reviewSearchParam.getOnlyInProgress()))
			.pageable(pageable)
			.build();
		return reviewService.getReviewsResList(request);
	}

	@Operation(summary = "리뷰 이미지 목록 조회", description = "main 화면에 표시되는 리뷰 이미지 목록 조회")
	@GetMapping("/images")
	public Page<ReviewsRes> getReviewImages(PageReq pageReq) {
		Pageable pageable = PageRequest.of(pageReq.getPageNumber(), pageReq.getPageSize());
		return reviewService.getReviewsResPage(pageable);
	}

	@Operation(summary = "나의 리뷰 목록 조회")
	@GetMapping("/myreview")
	public Page<MyReviewsRes> getMyReviews(PageReq pageReq) {
		Member member = MemberUtils.getAuthMember().getUser();
		Pageable pageable = PageRequest.of(pageReq.getPageNumber(), pageReq.getPageSize());
		MyReviewServiceReq request = MyReviewServiceReq.builder()
			.memberId(member.getId())
			.pageable(pageable)
			.build();

		return reviewService.getMyReviewsRes(request);
	}

	@Operation(summary = "이벤트의 리뷰 목록 조회")
	@GetMapping("/event/{eventId}")
	public Page<EventReviewsRes> getEventReviews(@PathVariable Long eventId, PageReq pageReq) {
		Pageable pageable = PageRequest.of(pageReq.getPageNumber(), pageReq.getPageSize());
		EventReviewServiceReq request = EventReviewServiceReq.builder()
			.eventId(eventId)
			.pageable(pageable)
			.build();

		return reviewService.getEventReviewsResList(request);
	}

}
