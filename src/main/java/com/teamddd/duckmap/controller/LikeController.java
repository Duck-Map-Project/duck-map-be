package com.teamddd.duckmap.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.dto.event.like.LikeRes;
import com.teamddd.duckmap.entity.EventLike;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.service.EventLikeService;
import com.teamddd.duckmap.util.MemberUtils;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class LikeController {
	private final EventLikeService eventLikeService;

	@Operation(summary = "이벤트 좋아요")
	@PostMapping("/{id}/likes")
	public LikeRes likeEvent(@PathVariable Long id) {
		Member member = MemberUtils.getAuthMember().getUser();
		EventLike eventLike = eventLikeService.likeEvent(id, member);
		return LikeRes.builder()
			.id(eventLike.getId())
			.build();
	}

	@Operation(summary = "이벤트 좋아요 취소")
	@DeleteMapping("/{id}/likes")
	public void deleteLikeEvent(@PathVariable Long id) {
		Member member = MemberUtils.getAuthMember().getUser();
		eventLikeService.deleteLikeEvent(id, member.getId());
	}
}
