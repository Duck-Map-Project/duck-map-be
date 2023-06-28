package com.teamddd.duckmap.dto.event.bookmark;

import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.entity.EventImage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkedEventRes {
	private Long id;
	private Long eventId;
	private String storeName;
	private ImageRes image;

	public static BookmarkedEventRes of(BookmarkEventDto bookmarkEventDto) {
		return BookmarkedEventRes.builder()
			.id(bookmarkEventDto.getEventBookmarkId())
			.eventId(bookmarkEventDto.getEventId())
			.storeName(bookmarkEventDto.getEventStoreName())
			.image(
				ImageRes.builder()
					.filename(
						bookmarkEventDto.getEventImageList().stream()
							.filter(EventImage::isThumbnail)
							.map(EventImage::getImage)
							.findFirst()
							.orElse(null)
					)
					.build()
			)
			.build();
	}
}
