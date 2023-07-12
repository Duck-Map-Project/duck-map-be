package com.teamddd.duckmap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PageReq {
	@Schema(defaultValue = "0")
	private int pageNumber;
	@Schema(defaultValue = "20")
	private int pageSize;
}
