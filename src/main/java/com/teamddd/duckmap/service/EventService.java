package com.teamddd.duckmap.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.dto.artist.ArtistRes;
import com.teamddd.duckmap.dto.event.category.EventCategoryRes;
import com.teamddd.duckmap.dto.event.event.CreateEventReq;
import com.teamddd.duckmap.dto.event.event.EventLikeBookmarkDto;
import com.teamddd.duckmap.dto.event.event.EventRes;
import com.teamddd.duckmap.dto.event.event.EventSearchServiceReq;
import com.teamddd.duckmap.dto.event.event.EventsRes;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventArtist;
import com.teamddd.duckmap.entity.EventCategory;
import com.teamddd.duckmap.entity.EventImage;
import com.teamddd.duckmap.entity.EventInfoCategory;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.NonExistentEventException;
import com.teamddd.duckmap.repository.EventLikeRepository;
import com.teamddd.duckmap.repository.EventRepository;
import com.teamddd.duckmap.repository.ReviewRepository;

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
	private final ReviewRepository reviewRepository;
	private final EventLikeRepository eventLikeRepository;

	public Event getEvent(Long eventId) throws NonExistentEventException {
		return eventRepository.findById(eventId)
			.orElseThrow(NonExistentEventException::new);
	}

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
			.build();

		artists.stream()
			.map(artist -> EventArtist.builder()
				.event(event)
				.artist(artist)
				.build())
			.forEach(eventArtist -> event.getEventArtists().add(eventArtist));

		categories.stream()
			.map(category -> EventInfoCategory.builder()
				.event(event)
				.eventCategory(category)
				.build())
			.forEach(eventInfoCategory -> event.getEventInfoCategories().add(eventInfoCategory));

		List<String> imageFilenames = createEventReq.getImageFilenames();
		for (int i = 0; i < imageFilenames.size(); i++) {
			event.getEventImages().add(
				EventImage.builder()
					.event(event)
					.image(imageFilenames.get(i))
					.thumbnail(i == 0)
					.build()
			);
		}

		eventRepository.save(event);

		return event.getId();
	}

	public EventRes getEventRes(Long eventId, Long memberId, LocalDate date) {
		EventLikeBookmarkDto eventLikeBookmarkDto = eventRepository.findByIdWithLikeAndBookmark(eventId, memberId)
			.orElseThrow(NonExistentEventException::new);

		Event event = eventLikeBookmarkDto.getEvent();
		Long likeId = eventLikeBookmarkDto.getLikeId();
		Long bookmarkId = eventLikeBookmarkDto.getBookmarkId();

		int likeCount = Math.toIntExact(eventLikeRepository.countByEventId(eventId));
		double score = reviewRepository.avgScoreByEvent(eventId).orElse(0.0);

		return EventRes.builder()
			.id(event.getId())
			.storeName(event.getStoreName())
			.inProgress(event.isInProgress(date))
			.fromDate(event.getFromDate())
			.toDate(event.getToDate())
			.address(event.getAddress())
			.businessHour(event.getBusinessHour())
			.hashtag(event.getHashtag())
			.twitterUrl(event.getTwitterUrl())
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
			.images(
				event.getEventImages().stream()
					.map(eventImage -> ImageRes.builder()
						.filename(eventImage.getImage())
						.build())
					.collect(Collectors.toList())
			)
			.score(score)
			.likeId(likeId)
			.bookmarkId(bookmarkId)
			.likeCount(likeCount)
			.build();
	}

	public Page<EventsRes> getEventsResList(EventSearchServiceReq request) {
		Artist searhArtist = artistService.getArtist(request.getArtistId());

		LocalDate searchDate = request.isOnlyInProgress() ? request.getDate() : null;
		Page<EventLikeBookmarkDto> eventLikeBookmarkDtos = eventRepository.findByArtistAndDate(searhArtist.getId(),
			searchDate, request.getMemberId(),
			request.getPageable());

		return eventLikeBookmarkDtos
			.map(eventLikeBookmarkDto -> EventsRes.of(eventLikeBookmarkDto, request.getDate()));
	}
}
