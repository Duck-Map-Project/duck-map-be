package com.teamddd.duckmap.dto.event.event;

import java.util.List;

import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.EventArtist;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventsMapRes {
	private Long id;
	private String storeName;
	private String address;
	private Long likeCount;
	private Long reviewCount;
	private List<String> artists;

	public static EventsMapRes of(EventLikeReviewCountDto dto) {
		return EventsMapRes.builder()
			.storeName(dto.getEvent().getStoreName())
			.address(dto.getEvent().getAddress())
			.likeCount(dto.getLikeCount())
			.reviewCount(dto.getReviewCount())
			.artists(
				dto.getEvent().getEventArtists().stream()
					.map(EventArtist::getArtist)
					.map(Artist::getName)
					.toList()
			)
			.build();
	}
}
