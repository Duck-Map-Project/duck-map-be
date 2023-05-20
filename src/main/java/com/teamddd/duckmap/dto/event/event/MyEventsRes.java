package com.teamddd.duckmap.dto.event.event;

import java.util.List;

import com.teamddd.duckmap.dto.ArtistRes;
import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.dto.event.category.EventCategoryRes;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyEventsRes {
	private Long id;
	private String storeName;
	private String address;
	private List<ArtistRes> artists;
	private List<EventCategoryRes> categories;
	private ImageRes image;
}
