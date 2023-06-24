package com.teamddd.duckmap.dto.event.event;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventSearchParam {
	private Long artistId;
	private boolean onlyInProgress;
}
