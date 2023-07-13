package com.teamddd.duckmap.dto.event.event;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateEventServiceReq {
	private Long id;
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
