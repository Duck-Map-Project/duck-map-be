package com.teamddd.duckmap.dto.event.event;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HashtagRes {
	private Long eventId;
	private String hashtag;
}
