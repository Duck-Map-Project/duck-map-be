package com.teamddd.duckmap.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.dto.CreateReviewReq;
import com.teamddd.duckmap.dto.CreateReviewRes;
import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.dto.MyReviewsRes;
import com.teamddd.duckmap.dto.Result;
import com.teamddd.duckmap.dto.ReviewRes;
import com.teamddd.duckmap.dto.UpdateReviewReq;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

	@Operation(summary = "리뷰 등록")
	@PostMapping
	public Result<CreateReviewRes> createReview(@Validated @RequestBody CreateReviewReq createReviewReq) {
		return Result.<CreateReviewRes>builder()
			.data(
				CreateReviewRes.builder()
					.id(1L)
					.build()
			)
			.build();
	}

	@Operation(summary = "리뷰 pk로 조회")
	@GetMapping("/{id}")
	public Result<ReviewRes> getReview(@PathVariable Long id) {
		ImageRes imageRes = ImageRes.builder()
			.apiUrl("/images/")
			.filename("filename.png")
			.build();

		return Result.<ReviewRes>builder()
			.data(
				ReviewRes.builder()
					.id(1L)
					.userProfile(imageRes)
					.username("user_nickname")
					.createdAt(LocalDateTime.now().minusDays(4))
					.score(5)
					.content("review content")
					.photos(List.of(
						imageRes,
						imageRes
					))
					.build()
			)
			.build();
	}

	@Operation(summary = "리뷰 수정")
	@PutMapping("/{id}")
	public Result<Void> updateReview(@PathVariable Long id, @Validated @RequestBody UpdateReviewReq updateReviewReq) {
		return Result.<Void>builder().build();
	}

	@Operation(summary = "리뷰 삭제")
	@DeleteMapping("/{id}")
	public Result<Void> deleteReview(@PathVariable Long id) {
		return Result.<Void>builder().build();
	}

	@Operation(summary = "나의 리뷰 목록 조회")
	@GetMapping("/myreview")
	public Page<MyReviewsRes> getMyReviews(Pageable pageable) {
		ImageRes imageRes = ImageRes.builder()
			.apiUrl("/images/")
			.filename("filename.png")
			.build();

		return new PageImpl<>(List.of(
			MyReviewsRes.builder()
				.id(1L)
				.eventId(1L)
				.eventImage(imageRes)
				.eventStoreName("이벤트 상호명")
				.createAt(LocalDateTime.now().minusDays(10))
				.score(4)
				.reviewImage(imageRes)
				.content("리뷰 내용")
				.build(),
			MyReviewsRes.builder()
				.id(2L)
				.eventId(2L)
				.eventImage(imageRes)
				.eventStoreName("이벤트 상호명2")
				.createAt(LocalDateTime.now().minusDays(8))
				.score(5)
				.reviewImage(imageRes)
				.content("리뷰 내용22")
				.build()
		));
	}
}
