package com.teamddd.duckmap.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class UpdateEventReq {
	@NotBlank
	private String storeName;
	@NotNull
	private LocalDate fromDate;
	@NotNull
	private LocalDate toDate;
	@NotBlank
	private String address;
	private String businessHour;
	private String hashtag;
	private String twitterUrl;
	@NotNull
	@Size(min = 1)
	private List<Long> artistIds;
	private List<Long> categoryIds;
	private List<String> imageFilenames;
}
