package com.teamddd.duckmap.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;

@Getter
public class UpdateEventReq {
	private String storeName;
	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	private String address;
	private String businessHour;
	private String hashtag;
	private String twitterUrl;
	private List<Long> artistIds;
	private List<Long> categoryIds;
	private List<String> imageFilenames;
}
