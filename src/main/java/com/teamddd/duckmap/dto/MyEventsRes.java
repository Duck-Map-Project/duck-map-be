package com.teamddd.duckmap.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyEventsRes {
	private Long id;
	private String storeName;
	private String address;
	private List<MockArtistRes> artists;
	private List<MockCategoryRes> categories;
	private ImageRes image;
}
