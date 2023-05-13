package com.teamddd.duckmap.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.dto.CreateEventReq;
import com.teamddd.duckmap.dto.CreateEventRes;
import com.teamddd.duckmap.dto.EventRes;
import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.dto.Result;
import com.teamddd.duckmap.dto.UpdateEventReq;
import com.teamddd.duckmap.dto.UpdateEventRes;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

	@Operation(summary = "이벤트 등록", description = "address 형식 변경 가능성 있음")
	@PostMapping
	public Result<CreateEventRes> createEvent(@Validated @RequestBody CreateEventReq createEventReq) {
		return Result.<CreateEventRes>builder()
			.data(
				CreateEventRes.builder()
					.id(1L)
					.build()
			)
			.build();
	}

	@Operation(summary = "이벤트 pk로 조회")
	@GetMapping("/{id}")
	public Result<EventRes> getEvent(@PathVariable Long id) {
		return Result.<EventRes>builder()
			.data(
				EventRes.builder()
					.id(id)
					.storeName("상호명")
					.fromDate(LocalDateTime.now().minusDays(2L))
					.toDate(LocalDateTime.now().plusDays(1L))
					.address("주소")
					.businessHour("10:00 - 18:00")
					.hashtag("#뫄뫄 #생일축하해")
					.twitterUrl("https://twitter.com/home?lang=ko")
					.artists(List.of())
					.categories(List.of())
					.images(List.of(
						ImageRes.builder()
							.apiUrl("/images/")
							.filename("filename.jpg")
							.build()
					))
					.build()
			)
			.build();
	}

	@Operation(summary = "이벤트 수정")
	@PutMapping("/{id}")
	public Result<UpdateEventRes> updateEvent(@PathVariable Long id, @RequestBody UpdateEventReq updateEventReq) {
		return Result.<UpdateEventRes>builder()
			.data(
				UpdateEventRes.builder()
					.id(id)
					.build()
			)
			.build();
	}

	@Operation(summary = "이벤트 삭제")
	@DeleteMapping("/{id}")
	public Result<Void> deleteEvent(@PathVariable Long id) {
		return Result.<Void>builder().build();
	}
}
