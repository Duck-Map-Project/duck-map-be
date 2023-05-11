package com.teamddd.duckmap.dto;

import java.time.LocalDate;

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
}
