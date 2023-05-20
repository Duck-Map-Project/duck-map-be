package com.teamddd.duckmap.dto.event.event;

import java.time.LocalDateTime;
import java.util.List;

import com.teamddd.duckmap.dto.ArtistRes;
import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.dto.ReviewRes;
import com.teamddd.duckmap.dto.event.category.EventCategoryRes;

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
	private List<ArtistRes> artists;
	private List<EventCategoryRes> categories;
	private List<ImageRes> images;

	private double score;
	private boolean like;
	private int likeCount;
	private boolean bookmark;

	private List<ReviewRes> reviews;
}
