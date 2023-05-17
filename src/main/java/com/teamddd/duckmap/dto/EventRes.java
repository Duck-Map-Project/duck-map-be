package com.teamddd.duckmap.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EventRes {
	private Long id;
	private String storeName;
	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	private String address;
	private String businessHour;
	private String hashtag;
	private String twitterUrl;
	private List<MockArtistRes> artists;
	private List<MockCategoryRes> categories;
	private List<ImageRes> images;
}
