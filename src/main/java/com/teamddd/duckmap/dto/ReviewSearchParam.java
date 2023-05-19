package com.teamddd.duckmap.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewSearchParam {
	private Long artistId;
	private boolean inProgress;
}
