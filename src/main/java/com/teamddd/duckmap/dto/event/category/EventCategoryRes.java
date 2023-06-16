package com.teamddd.duckmap.dto.event.category;

import com.teamddd.duckmap.entity.EventCategory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventCategoryRes {
	private Long id;
	private String category;

	public static EventCategoryRes of(EventCategory eventCategory) {
		return EventCategoryRes.builder()
			.id(eventCategory.getId())
			.category(eventCategory.getCategory())
			.build();
	}
}
