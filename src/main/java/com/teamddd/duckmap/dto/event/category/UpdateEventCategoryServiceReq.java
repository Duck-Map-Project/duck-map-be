package com.teamddd.duckmap.dto.event.category;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateEventCategoryServiceReq {
	private Long id;
	private String category;
}
