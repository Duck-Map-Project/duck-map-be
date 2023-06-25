package com.teamddd.duckmap.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewSearchParam {
	private Long artistId;
	@Schema(defaultValue = "false")
	private Boolean onlyInProgress;
}
