package com.teamddd.duckmap.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.event.CreateEventReq;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventArtist;
import com.teamddd.duckmap.entity.EventCategory;
import com.teamddd.duckmap.entity.EventImage;
import com.teamddd.duckmap.entity.EventInfoCategory;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.repository.EventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

	private final EventRepository eventRepository;
	private final ArtistService artistService;
	private final EventCategoryService eventCategoryService;

	@Transactional
	public Long createEvent(CreateEventReq createEventReq, Member member) {
		List<Artist> artists = artistService.getArtistsByIds(createEventReq.getArtistIds());
		List<EventCategory> categories = eventCategoryService.getEventCategoriesByIds(
			createEventReq.getCategoryIds());

		Event event = Event.builder()
			.member(member)
			.storeName(createEventReq.getStoreName())
			.fromDate(createEventReq.getFromDate())
			.toDate(createEventReq.getToDate())
			.address(createEventReq.getAddress())
			.businessHour(createEventReq.getBusinessHour())
			.hashtag(createEventReq.getHashtag())
			.twitterUrl(createEventReq.getTwitterUrl())
			.eventArtists(
				artists.stream()
					.map(artist -> EventArtist.builder()
						.artist(artist)
						.build())
					.collect(Collectors.toList())
			)
			.eventInfoCategories(
				categories.stream()
					.map(category -> EventInfoCategory.builder()
						.eventCategory(category)
						.build())
					.collect(Collectors.toList())
			)
			.eventImages(
				createEventReq.getImageFilenames().stream()
					.map(filename -> EventImage.builder()
						.image(filename)
						.build())
					.collect(Collectors.toList())
			)
			.build();

		eventRepository.save(event);

		return event.getId();
	}
}
