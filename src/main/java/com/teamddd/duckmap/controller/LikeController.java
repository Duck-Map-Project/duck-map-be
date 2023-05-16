package com.teamddd.duckmap.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.dto.LikeReq;
import com.teamddd.duckmap.dto.LikeRes;
import com.teamddd.duckmap.dto.Result;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events/{id}/likes")
public class LikeController {

	@Operation(summary = "이벤트 좋아요")
	@PostMapping
	public Result<LikeRes> likeEvent(@Validated @RequestBody LikeReq likeReq) {
		return Result.<LikeRes>builder()
			.data(
				LikeRes.builder()
					.id(1L)
					.build()
			)
			.build();
	}

	@Operation(summary = "이벤트 좋아요 취소")
	public Result<Void> dislikeEvent(@PathVariable Long id) {
		return Result.<Void>builder().build();
	}
}
