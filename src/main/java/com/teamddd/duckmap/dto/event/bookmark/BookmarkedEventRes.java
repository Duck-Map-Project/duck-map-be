package com.teamddd.duckmap.dto.event.bookmark;

import com.teamddd.duckmap.dto.ImageRes;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkedEventRes {
	private Long id;
	private String storeName;
	private ImageRes image;
}
