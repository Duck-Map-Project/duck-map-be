package com.teamddd.duckmap.dto.event.event;

import java.time.LocalDate;
import java.util.List;

import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.dto.artist.ArtistRes;
import com.teamddd.duckmap.dto.event.category.EventCategoryRes;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EventRes {
	private Long id;
	private String storeName;
	private boolean inProgress;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String address;
	private String businessHour;
	private String hashtag;
	private String twitterUrl;
	private List<ArtistRes> artists;
	private List<EventCategoryRes> categories;
	private List<ImageRes> images;

	private double score;
	private Long likeId;
	private int likeCount;
	private Long bookmarkId;
}
