package com.teamddd.duckmap.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.config.security.SecurityRule;
import com.teamddd.duckmap.dto.event.category.CreateEventCategoryReq;
import com.teamddd.duckmap.dto.event.category.CreateEventCategoryRes;
import com.teamddd.duckmap.dto.event.category.EventCategoryRes;
import com.teamddd.duckmap.dto.event.category.UpdateEventCategoryReq;
import com.teamddd.duckmap.dto.event.category.UpdateEventCategoryServiceReq;
import com.teamddd.duckmap.service.EventCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events/categories")
public class EventCategoryController {

	private final EventCategoryService eventCategoryService;

	@PreAuthorize(SecurityRule.HAS_ROLE_ADMIN)
	@Operation(summary = "이벤트 카테고리 생성")
	@PostMapping
	public CreateEventCategoryRes createEventCategory(
		@Validated @RequestBody CreateEventCategoryReq createEventCategoryReq) {
		Long categoryId = eventCategoryService.createEventCategory(createEventCategoryReq);
		return CreateEventCategoryRes.builder()
			.id(categoryId)
			.build();
	}

	@Operation(summary = "이벤트 카테고리 목록 조회")
	@GetMapping
	public List<EventCategoryRes> getEventCategories() {
		return eventCategoryService.getEventCategoryResList();
	}

	@PreAuthorize(SecurityRule.HAS_ROLE_ADMIN)
	@Operation(summary = "이벤트 카테고리 수정")
	@PutMapping("/{id}")
	public void updateEventCategory(@PathVariable Long id,
		@Validated @RequestBody UpdateEventCategoryReq updateEventCategoryReq) {
		UpdateEventCategoryServiceReq request = UpdateEventCategoryServiceReq.builder()
			.id(id)
			.category(updateEventCategoryReq.getCategory())
			.build();

		eventCategoryService.updateEventCategory(request);
	}

	@PreAuthorize(SecurityRule.HAS_ROLE_ADMIN)
	@Operation(summary = "이벤트 카테고리 삭제")
	@DeleteMapping("/{id}")
	public void deleteEventCategory(@PathVariable Long id) {
		eventCategoryService.deleteEventCategory(id);
	}
}
