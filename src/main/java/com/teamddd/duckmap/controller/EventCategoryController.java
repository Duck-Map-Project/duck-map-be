package com.teamddd.duckmap.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.dto.Result;
import com.teamddd.duckmap.dto.event.category.CreateEventCategoryReq;
import com.teamddd.duckmap.dto.event.category.CreateEventCategoryRes;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events/categories")
public class EventCategoryController {

	@Operation(summary = "이벤트 카테고리 생성")
	@PostMapping
	public Result<CreateEventCategoryRes> createEventCategory(
		@Validated @RequestBody CreateEventCategoryReq createEventCategoryReq) {
		return Result.<CreateEventCategoryRes>builder()
			.data(
				CreateEventCategoryRes.builder()
					.id(1L)
					.build()
			)
			.build();
	}
}
