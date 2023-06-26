package com.teamddd.duckmap.dto.event.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventSearchParam {
	private Long artistId;
	@Schema(defaultValue = "false")
	private Boolean onlyInProgress;
}
