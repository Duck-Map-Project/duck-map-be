package com.teamddd.duckmap.controller;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Page;
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

import com.teamddd.duckmap.dto.event.event.CreateEventReq;
import com.teamddd.duckmap.dto.event.event.CreateEventRes;
import com.teamddd.duckmap.dto.event.event.EventRes;
import com.teamddd.duckmap.dto.event.event.EventSearchParam;
import com.teamddd.duckmap.dto.event.event.EventSearchServiceReq;
import com.teamddd.duckmap.dto.event.event.EventsRes;
import com.teamddd.duckmap.dto.event.event.HashtagRes;
import com.teamddd.duckmap.dto.event.event.MyEventsServiceReq;
import com.teamddd.duckmap.dto.event.event.MyLikeEventsServiceReq;
import com.teamddd.duckmap.dto.event.event.UpdateEventReq;
import com.teamddd.duckmap.dto.event.event.UpdateEventServiceReq;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.service.EventService;
import com.teamddd.duckmap.util.MemberUtils;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

	private final EventService eventService;

	@Operation(summary = "이벤트 등록", description = "address 형식 변경 가능성 있음")
	@PostMapping
	public CreateEventRes createEvent(@Validated @RequestBody CreateEventReq createEventReq) {
		Member member = MemberUtils.getAuthMember().getUser();

		Long eventId = eventService.createEvent(createEventReq, member);

		return CreateEventRes.builder()
			.id(eventId)
			.build();
	}

	@Operation(summary = "이벤트 pk로 조회")
	@GetMapping("/{id}")
	public EventRes getEvent(@PathVariable Long id) {
		Long memberId = MemberUtils.getMember()
			.map(Member::getId)
			.orElse(null);

		LocalDate today = LocalDate.now();

		return eventService.getEventRes(id, memberId, today);
	}

	@Operation(summary = "이벤트 수정")
	@PutMapping("/{id}")
	public void updateEvent(@PathVariable Long id, @Validated @RequestBody UpdateEventReq updateEventReq) {
		Member member = MemberUtils.getAuthMember().getUser();

		UpdateEventServiceReq request = updateEventReq.toServiceRequest(id);
		eventService.updateEvent(member.getId(), request);
	}

	@Operation(summary = "이벤트 삭제")
	@DeleteMapping("/{id}")
	public void deleteEvent(@PathVariable Long id) {
		Member member = MemberUtils.getAuthMember().getUser();

		eventService.deleteEvent(member.getId(), id);
	}

	@Operation(summary = "이벤트 목록 조회")
	@GetMapping
	public Page<EventsRes> getEvents(Pageable pageable, @ModelAttribute EventSearchParam eventSearchParam) {
		Long memberId = MemberUtils.getMember()
			.map(Member::getId)
			.orElse(null);

		LocalDate today = LocalDate.now();

		EventSearchServiceReq request = EventSearchServiceReq.builder()
			.memberId(memberId)
			.date(today)
			.artistId(eventSearchParam.getArtistId())
			.onlyInProgress(BooleanUtils.isTrue(eventSearchParam.getOnlyInProgress()))
			.pageable(pageable)
			.build();

		return eventService.getEventsResList(request);
	}

	@Operation(summary = "나의 이벤트 목록 조회")
	@GetMapping("/myevent")
	public Page<EventsRes> getMyEvents(Pageable pageable) {
		Member member = MemberUtils.getAuthMember().getUser();
		LocalDate today = LocalDate.now();

		MyEventsServiceReq request = MyEventsServiceReq.builder()
			.memberId(member.getId())
			.pageable(pageable)
			.date(today)
			.build();

		return eventService.getMyEventsRes(request);
	}

	@Operation(summary = "나의 좋아요 이벤트 목록 조회")
	@GetMapping("/mylike")
	public Page<EventsRes> getMyLikeEvents(Pageable pageable) {
		Member member = MemberUtils.getAuthMember().getUser();
		LocalDate today = LocalDate.now();

		MyLikeEventsServiceReq request = MyLikeEventsServiceReq.builder()
			.memberId(member.getId())
			.date(today)
			.pageable(pageable)
			.build();

		return eventService.getMyLikeEventsRes(request);
	}

	@Operation(summary = "오늘 진행중인 이벤트 해시태그 목록 조회")
	@GetMapping("/hashtags/today")
	public List<HashtagRes> getTodayHashtags() {
		return List.of(
			HashtagRes.builder()
				.eventId(1L)
				.hashtag("#뫄뫄 #생일_축하해")
				.build(),
			HashtagRes.builder()
				.eventId(2L)
				.hashtag("#소녀시대 #10주년")
				.build(),
			HashtagRes.builder()
				.eventId(3L)
				.hashtag("#뫄뫄_탄신 #벌써10000일")
				.build(),
			HashtagRes.builder()
				.eventId(4L)
				.hashtag("#드라마 #대박나자")
				.build()
		);
	}
}
