package com.teamddd.duckmap.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PageReq {
	@Builder.Default
	private int pageNumber = 0;
	@Builder.Default
	private int pageSize = 20;
}
