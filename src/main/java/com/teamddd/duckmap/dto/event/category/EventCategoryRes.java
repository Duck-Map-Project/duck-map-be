package com.teamddd.duckmap.dto.event.category;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventCategoryRes {
	private Long id;
	private String category;
}
