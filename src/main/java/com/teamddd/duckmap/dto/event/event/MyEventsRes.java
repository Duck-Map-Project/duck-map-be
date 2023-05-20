package com.teamddd.duckmap.dto.event.event;

import java.util.List;

import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.dto.MockArtistRes;
import com.teamddd.duckmap.dto.MockCategoryRes;

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
