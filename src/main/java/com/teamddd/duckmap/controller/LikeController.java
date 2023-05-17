package com.teamddd.duckmap.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.dto.LikeRes;
import com.teamddd.duckmap.dto.Result;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class LikeController {

	@Operation(summary = "이벤트 좋아요")
	@PostMapping("/{id}/likes")
	public Result<LikeRes> likeEvent(@PathVariable Long id, HttpSession session) {
		return Result.<LikeRes>builder()
			.data(
				LikeRes.builder()
					.id(1L)
					.build()
			)
			.build();
	}

	@Operation(summary = "이벤트 좋아요 취소")
	@DeleteMapping("/{id}/likes")
	public Result<Void> deleteLikeEvent(@PathVariable Long id) {
		return Result.<Void>builder().build();
	}
}
