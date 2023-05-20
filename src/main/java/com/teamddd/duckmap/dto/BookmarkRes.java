package com.teamddd.duckmap.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkRes {
	private Long id;
	private EventRes event;
}
