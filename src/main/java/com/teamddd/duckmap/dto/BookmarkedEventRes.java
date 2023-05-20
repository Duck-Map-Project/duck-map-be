package com.teamddd.duckmap.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkedEventRes {
	private Long id;
	private String storeName;
	private ImageRes image;
}
