package com.teamddd.duckmap.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeReq {
	private Long userId;
	private Long eventId;

	public LikeReq(Long userId, Long eventId) {
		this.userId = userId;
		this.eventId = eventId;
	}
}
