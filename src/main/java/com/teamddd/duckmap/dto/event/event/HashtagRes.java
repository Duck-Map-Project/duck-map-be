package com.teamddd.duckmap.dto.event.event;

import com.teamddd.duckmap.entity.Event;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HashtagRes {
	private Long eventId;
	private String hashtag;

	public static HashtagRes of(Event event) {
		return HashtagRes.builder()
			.eventId(event.getId())
			.hashtag(event.getHashtag())
			.build();
	}
}
