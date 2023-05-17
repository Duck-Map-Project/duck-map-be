package com.teamddd.duckmap.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.dto.CreateReviewReq;
import com.teamddd.duckmap.dto.CreateReviewRes;
import com.teamddd.duckmap.dto.Result;

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
}
