package com.teamddd.duckmap.dto.event.event;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.dto.artist.ArtistRes;
import com.teamddd.duckmap.dto.event.category.EventCategoryRes;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventArtist;
import com.teamddd.duckmap.entity.EventImage;
import com.teamddd.duckmap.entity.EventInfoCategory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventsRes {
	private Long id;
	private String storeName;
	private boolean inProgress;
	private String address;
	private List<ArtistRes> artists;
	private List<EventCategoryRes> categories;
	private ImageRes image;
	private Long likeId;
	private Long bookmarkId;

	public static EventsRes of(EventLikeBookmarkDto eventLikeBookmarkDto, LocalDate date) {
		Event event = eventLikeBookmarkDto.getEvent();
		Long likeId = eventLikeBookmarkDto.getLike() == null ? null : eventLikeBookmarkDto.getLike().getId();
		Long bookmarkId =
			eventLikeBookmarkDto.getBookmark() == null ? null : eventLikeBookmarkDto.getBookmark().getId();

		return EventsRes.builder()
			.id(event.getId())
			.storeName(event.getStoreName())
			.inProgress(event.isInProgress(date))
			.address(event.getAddress())
			.artists(
				event.getEventArtists().stream()
					.map(EventArtist::getArtist)
					.map(ArtistRes::of)
					.collect(Collectors.toList())
			)
			.categories(
				event.getEventInfoCategories().stream()
					.map(EventInfoCategory::getEventCategory)
					.map(EventCategoryRes::of)
					.collect(Collectors.toList())
			)
			.image(
				ImageRes.builder()
					.filename(
						event.getEventImages().stream()
							.filter(EventImage::isThumbnail)
							.map(EventImage::getImage)
							.findFirst()
							.orElse(null)
					)
					.build()
			)
			.likeId(likeId)
			.bookmarkId(bookmarkId)
			.build();
	}
}
