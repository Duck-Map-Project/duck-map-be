package com.teamddd.duckmap.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;

@Getter
public class CreateEventReq {
	private String storeName;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String address;
	private String businessHour;
	private String hashtag;
	private String twitterUrl;
	private List<Long> artistIds;
	private List<Long> categoryIds;
	private List<String> imageFilenames;
}
