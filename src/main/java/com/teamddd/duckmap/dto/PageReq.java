package com.teamddd.duckmap.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PageReq {
	private Long offset;
	private int pageNumber;
	private int pageSize;
}
