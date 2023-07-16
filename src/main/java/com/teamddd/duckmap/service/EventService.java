package com.teamddd.duckmap.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.teamddd.duckmap.common.ApiUrl;
import com.teamddd.duckmap.common.Props;
import com.teamddd.duckmap.dto.artist.ArtistRes;
import com.teamddd.duckmap.dto.event.category.EventCategoryRes;
import com.teamddd.duckmap.dto.event.event.CreateEventReq;
import com.teamddd.duckmap.dto.event.event.EventLikeBookmarkDto;
import com.teamddd.duckmap.dto.event.event.EventLikeReviewCountDto;
import com.teamddd.duckmap.dto.event.event.EventRes;
import com.teamddd.duckmap.dto.event.event.EventSearchServiceReq;
import com.teamddd.duckmap.dto.event.event.EventsMapRes;
import com.teamddd.duckmap.dto.event.event.EventsRes;
import com.teamddd.duckmap.dto.event.event.HashtagRes;
import com.teamddd.duckmap.dto.event.event.MyEventsServiceReq;
import com.teamddd.duckmap.dto.event.event.MyLikeEventsServiceReq;
import com.teamddd.duckmap.dto.event.event.UpdateEventServiceReq;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventArtist;
import com.teamddd.duckmap.entity.EventCategory;
import com.teamddd.duckmap.entity.EventImage;
import com.teamddd.duckmap.entity.EventInfoCategory;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.NonExistentEventException;
import com.teamddd.duckmap.repository.BookmarkRepository;
import com.teamddd.duckmap.repository.EventArtistRepository;
import com.teamddd.duckmap.repository.EventImageRepository;
import com.teamddd.duckmap.repository.EventInfoCategoryRepository;
import com.teamddd.duckmap.repository.EventLikeRepository;
import com.teamddd.duckmap.repository.EventRepository;
import com.teamddd.duckmap.repository.ReviewRepository;
import com.teamddd.duckmap.util.FileUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {
	private final Props props;
	private final EventRepository eventRepository;
	private final ArtistService artistService;
	private final EventCategoryService eventCategoryService;
	private final LastSearchArtistService lastSearchArtistService;
	private final ReviewRepository reviewRepository;
	private final EventLikeRepository eventLikeRepository;
	private final BookmarkRepository eventBookmarkRepository;
	private final EventArtistRepository eventArtistRepository;
	private final EventInfoCategoryRepository eventInfoCategoryRepository;
	private final EventImageRepository eventImageRepository;

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
					.map(EventImage::getImage)
					.map(image -> ApiUrl.IMAGE + image)
					.collect(Collectors.toList())
			)
			.score(score)
			.likeId(likeId)
			.bookmarkId(bookmarkId)
			.likeCount(likeCount)
			.build();
	}

	public Page<EventsRes> getEventsResList(EventSearchServiceReq request) {
		if (request.getArtistId() != null) {
			Artist searchArtist = artistService.getArtist(request.getArtistId());

			request.getMember()
				.ifPresent(member -> lastSearchArtistService.saveLastSearchArtist(member, searchArtist));
		}

		LocalDate searchDate = request.isOnlyInProgress() ? request.getDate() : null;
		Page<EventLikeBookmarkDto> eventLikeBookmarkDtos = eventRepository.findByArtistAndDate(request.getArtistId(),
			searchDate,
			request.getMember().map(Member::getId).orElse(null),
			request.getPageable());

		return eventLikeBookmarkDtos
			.map(eventLikeBookmarkDto -> EventsRes.of(eventLikeBookmarkDto, request.getDate()));
	}

	public Page<EventsRes> getMyEventsRes(MyEventsServiceReq request) {
		Page<EventLikeBookmarkDto> myEvents = eventRepository.findMyEvents(request.getMemberId(),
			request.getPageable());

		return myEvents.map(eventLikeBookmarkDto -> EventsRes.of(eventLikeBookmarkDto, request.getDate()));
	}

	public Page<EventsRes> getMyLikeEventsRes(MyLikeEventsServiceReq request) {
		Page<EventLikeBookmarkDto> myLikeEvents = eventRepository.findMyLikeEvents(request.getMemberId(),
			request.getPageable());

		return myLikeEvents.map(eventLikeBookmarkDto -> EventsRes.of(eventLikeBookmarkDto, request.getDate()));
	}

	public Page<EventsMapRes> getEventsForMap(LocalDate date, Pageable pageable) {
		Page<EventLikeReviewCountDto> events = eventRepository.findForMap(date, pageable);

		return events.map(EventsMapRes::of);
	}

	public List<HashtagRes> getHashtagResListByDate(LocalDate date) {
		return eventRepository.findByArtistAndDate(date).stream()
			.map(HashtagRes::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public void updateEvent(Long memberId, UpdateEventServiceReq request) {
		Event event = getEvent(request.getId());
		if (!memberId.equals(event.getMember().getId())) {
			throw new NonExistentEventException();
		}

		// Artist pk, EventCategory pk 유효한 값인지 조회
		List<Artist> newArtists = artistService.getArtistsByIds(request.getArtistIds());
		List<EventCategory> newCategories = eventCategoryService.getEventCategoriesByIds(
			request.getCategoryIds());

		List<String> oldImages = event.getEventImages().stream()
			.map(EventImage::getImage)
			.collect(Collectors.toList());

		// EventArtist를 덮어쓴다
		eventArtistRepository.deleteByEventId(event.getId());
		eventArtistRepository.saveAll(
			newArtists.stream()
				.map(artist ->
					EventArtist.builder()
						.event(event)
						.artist(artist)
						.build())
				.collect(Collectors.toList())
		);

		// EventInfoCategory를 덮어쓴다
		eventInfoCategoryRepository.deleteByEventId(event.getId());
		eventInfoCategoryRepository.saveAll(
			newCategories.stream()
				.map(category -> EventInfoCategory.builder()
					.event(event)
					.eventCategory(category)
					.build())
				.collect(Collectors.toList())
		);

		// EventImage를 덮어쓴다
		eventImageRepository.deleteByEventId(event.getId());
		List<EventImage> newEventImages = new ArrayList<>();
		List<String> imageFilenames = request.getImageFilenames();
		for (int i = 0; i < imageFilenames.size(); i++) {
			newEventImages.add(EventImage.builder()
				.event(event)
				.thumbnail(i == 0)
				.image(imageFilenames.get(i))
				.build());
		}
		eventImageRepository.saveAll(newEventImages);

		event.updateEvent(request.getStoreName(), request.getFromDate(), request.getToDate(), request.getAddress(),
			request.getBusinessHour(), request.getHashtag(), request.getTwitterUrl());

		// 사용되지 않는 기존 이미지 제거
		for (String oldImage : oldImages) {
			if (!imageFilenames.contains(oldImage)) {
				FileUtils.deleteFile(props.getImageDir(), oldImage);
			}
		}
	}

	@Transactional
	public void deleteEvent(Long memberId, Long id) {
		Event event = getEvent(id);
		if (!memberId.equals(event.getMember().getId())) {
			throw new NonExistentEventException();
		}

		List<String> filenames = event.getEventImages().stream()
			.map(EventImage::getImage)
			.collect(Collectors.toList());

		eventArtistRepository.deleteByEventId(id);
		eventInfoCategoryRepository.deleteByEventId(id);
		eventImageRepository.deleteByEventId(id);

		eventLikeRepository.deleteByEventId(id);
		eventBookmarkRepository.deleteByEventId(id);

		eventRepository.deleteById(id);

		for (String filename : filenames) {
			if (StringUtils.hasText(filename)) {
				FileUtils.deleteFile(props.getImageDir(), filename);
			}
		}
	}
}
