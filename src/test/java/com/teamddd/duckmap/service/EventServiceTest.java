package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.event.CreateEventReq;
import com.teamddd.duckmap.dto.event.event.EventRes;
import com.teamddd.duckmap.dto.event.event.EventSearchServiceReq;
import com.teamddd.duckmap.dto.event.event.EventsMapRes;
import com.teamddd.duckmap.dto.event.event.EventsRes;
import com.teamddd.duckmap.dto.event.event.HashtagRes;
import com.teamddd.duckmap.dto.event.event.MyEventsServiceReq;
import com.teamddd.duckmap.dto.event.event.MyLikeEventsServiceReq;
import com.teamddd.duckmap.dto.event.event.UpdateEventServiceReq;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.ArtistType;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventArtist;
import com.teamddd.duckmap.entity.EventBookmark;
import com.teamddd.duckmap.entity.EventCategory;
import com.teamddd.duckmap.entity.EventImage;
import com.teamddd.duckmap.entity.EventInfoCategory;
import com.teamddd.duckmap.entity.EventLike;
import com.teamddd.duckmap.entity.LastSearchArtist;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.entity.Review;
import com.teamddd.duckmap.entity.Role;
import com.teamddd.duckmap.repository.EventInfoCategoryRepository;
import com.teamddd.duckmap.repository.EventLikeRepository;
import com.teamddd.duckmap.repository.EventRepository;
import com.teamddd.duckmap.repository.LastSearchArtistRepository;
import com.teamddd.duckmap.repository.ReviewRepository;

@Transactional
@SpringBootTest
class EventServiceTest {
	@Autowired
	EntityManager em;
	@Autowired
	EventService eventService;
	@SpyBean
	EventRepository eventRepository;
	@MockBean
	ArtistService artistService;
	@MockBean
	EventCategoryService eventCategoryService;
	@MockBean
	ReviewRepository reviewRepository;
	@SpyBean
	EventLikeRepository eventLikeRepository;
	@Autowired
	EventInfoCategoryRepository eventInfoCategoryRepository;
	@Autowired
	LastSearchArtistRepository lastSearchArtistRepository;

	@DisplayName("이벤트를 생성한다")
	@Test
	void createEvent() throws Exception {
		//given
		Artist artist1 = createArtist("artist1", null);
		em.persist(artist1);
		EventCategory category1 = createEventCategory("category1");
		em.persist(category1);

		CreateEventReq request = new CreateEventReq();
		ReflectionTestUtils.setField(request, "storeName", "store name");
		ReflectionTestUtils.setField(request, "fromDate", LocalDate.now());
		ReflectionTestUtils.setField(request, "toDate", LocalDate.now());
		ReflectionTestUtils.setField(request, "address", "address");
		ReflectionTestUtils.setField(request, "artistIds", List.of(1L));
		ReflectionTestUtils.setField(request, "categoryIds", List.of(1L));
		ReflectionTestUtils.setField(request, "imageFilenames", List.of("filename"));

		Member member = Member.builder().username("member1").build();

		List<Artist> artists = List.of(artist1);
		List<EventCategory> categories = List.of(category1);
		when(artistService.getArtistsByIds(any())).thenReturn(artists);
		when(eventCategoryService.getEventCategoriesByIds(any())).thenReturn(categories);

		//when
		Long eventId = eventService.createEvent(request, member);

		//then
		assertThat(eventId).isNotNull();

		Optional<Event> findEvent = eventRepository.findById(eventId);
		assertThat(findEvent).isNotEmpty();
		assertThat(findEvent.get()).extracting("storeName", "member.username")
			.containsOnly("store name", "member1");
		assertThat(findEvent.get().getEventArtists()).hasSize(1)
			.extracting("artist.name").containsExactlyInAnyOrder("artist1");
		assertThat(findEvent.get().getEventInfoCategories()).hasSize(1)
			.extracting("eventCategory.category").containsExactlyInAnyOrder("category1");
	}

	@DisplayName("Event Id로 EventRes 조회")
	@Test
	void getEventRes() throws Exception {
		//given
		LocalDate now = LocalDate.now();

		// save Artist
		ArtistType artistType = createArtistType();
		em.persist(artistType);
		Artist artist1 = createArtist("artist1", artistType);
		Artist artist2 = createArtist("artist2", artistType);
		em.persist(artist1);
		em.persist(artist2);

		// save EventCategory
		EventCategory eventCategory1 = createEventCategory("event_category1");
		EventCategory eventCategory2 = createEventCategory("event_category2");
		em.persist(eventCategory1);
		em.persist(eventCategory2);

		// save Event
		Event event = createEvent(null, "event1", now, now.plusDays(2), "#hashtag");
		em.persist(event);
		Long eventId = event.getId();

		// Artist, EventCategory -> event
		EventArtist eventArtist1 = createEventArtist(event, artist1);
		EventArtist eventArtist2 = createEventArtist(event, artist2);
		em.persist(eventArtist1);
		em.persist(eventArtist2);
		EventInfoCategory eventInfoCategory1 = createEventInfoCategory(event, eventCategory1);
		EventInfoCategory eventInfoCategory2 = createEventInfoCategory(event, eventCategory2);
		em.persist(eventInfoCategory1);
		em.persist(eventInfoCategory2);

		em.flush();
		em.clear();

		// MockBean
		Long likeCount = 12L;
		Optional<Double> score = Optional.of(4.4);
		when(eventLikeRepository.countByEventId(eventId)).thenReturn(likeCount);
		when(reviewRepository.avgScoreByEvent(eventId)).thenReturn(score);

		//when
		EventRes eventRes = eventService.getEventRes(eventId, null, now);

		//then
		assertThat(eventRes).extracting("id", "storeName", "inProgress", "likeId", "bookmarkId", "score", "likeCount")
			.containsOnly(eventId, "event1", true, null, null, 4.4, 12);

		assertThat(eventRes.getArtists()).extracting("name", "artistType.type")
			.containsExactlyInAnyOrder(Tuple.tuple("artist1", "artist_type"), Tuple.tuple("artist2", "artist_type"));
		assertThat(eventRes.getCategories()).extracting("category")
			.containsExactlyInAnyOrder("event_category1", "event_category2");
	}

	@DisplayName("Member Id로 EventsRes 목록 조회")
	@Test
	void getMyEventsRes() throws Exception {
		//given
		LocalDate now = LocalDate.now();

		Member member1 = createMember("member1");
		Member member2 = createMember("member2");
		em.persist(member1);
		em.persist(member2);

		// save Artist
		ArtistType artistType = createArtistType();
		em.persist(artistType);
		Artist artist1 = createArtist("artist1", artistType);
		Artist artist2 = createArtist("artist2", artistType);
		em.persist(artist1);
		em.persist(artist2);

		// save EventCategory
		EventCategory eventCategory1 = createEventCategory("event_category1");
		EventCategory eventCategory2 = createEventCategory("event_category2");
		em.persist(eventCategory1);
		em.persist(eventCategory2);

		//save Event
		Event event1 = createEvent(member1, "event1", now.minusDays(2), now.plusDays(1), "");
		Event event2 = createEvent(member2, "event2", now.minusDays(2), now.plusDays(1), "");
		Event event3 = createEvent(member1, "event3", now.plusDays(1), now.plusDays(1), "");
		Event event4 = createEvent(member2, "event4", now.plusDays(1), now.plusDays(1), "");
		em.persist(event1);
		em.persist(event2);
		em.persist(event3);
		em.persist(event4);

		// Artist, EventCategory, EventImage -> event
		EventArtist eventArtist1 = createEventArtist(event1, artist1);
		EventArtist eventArtist2 = createEventArtist(event2, artist1);
		EventArtist eventArtist3 = createEventArtist(event2, artist2);
		EventArtist eventArtist4 = createEventArtist(event3, artist2);
		EventArtist eventArtist5 = createEventArtist(event4, artist1);
		em.persist(eventArtist1);
		em.persist(eventArtist2);
		em.persist(eventArtist3);
		em.persist(eventArtist4);
		em.persist(eventArtist5);
		EventInfoCategory eventInfoCategory1 = createEventInfoCategory(event1, eventCategory1);
		EventInfoCategory eventInfoCategory2 = createEventInfoCategory(event2, eventCategory2);
		EventInfoCategory eventInfoCategory3 = createEventInfoCategory(event3, eventCategory2);
		EventInfoCategory eventInfoCategory4 = createEventInfoCategory(event4, eventCategory1);
		em.persist(eventInfoCategory1);
		em.persist(eventInfoCategory2);
		em.persist(eventInfoCategory3);
		em.persist(eventInfoCategory4);
		EventImage image1 = createEventImage(event1, "image1", false);
		EventImage image2 = createEventImage(event1, "image2", true);
		EventImage image3 = createEventImage(event2, "image3", true);
		EventImage image4 = createEventImage(event2, "image4", false);
		EventImage image5 = createEventImage(event3, "image5", true);
		EventImage image6 = createEventImage(event4, "image6", true);
		em.persist(image1);
		em.persist(image2);
		em.persist(image3);
		em.persist(image4);
		em.persist(image5);
		em.persist(image6);

		em.flush();
		em.clear();

		Long searchMemberId = member2.getId();
		Pageable pageable = PageRequest.of(0, 3);

		MyEventsServiceReq request = MyEventsServiceReq.builder()
			.memberId(searchMemberId)
			.date(now)
			.pageable(pageable)
			.build();
		//when
		Page<EventsRes> myEventsResList = eventService.getMyEventsRes(request);

		//then
		assertThat(myEventsResList).hasSize(2)
			.extracting("storeName", "inProgress")
			.containsExactlyInAnyOrder(
				Tuple.tuple("event2", true),
				Tuple.tuple("event4", false));
	}

	@DisplayName("Member Id가 좋아요한 EventsRes 목록 조회")
	@Test
	void getMyLikeEventsRes() throws Exception {
		//given
		LocalDate now = LocalDate.now();

		// save Member
		Member member1 = createMember("member1");
		Member member2 = createMember("member2");
		em.persist(member1);
		em.persist(member2);

		// save Artist
		ArtistType artistType = createArtistType();
		em.persist(artistType);
		Artist artist1 = createArtist("artist1", artistType);
		Artist artist2 = createArtist("artist2", artistType);
		em.persist(artist1);
		em.persist(artist2);

		// save EventCategory
		EventCategory eventCategory1 = createEventCategory("event_category1");
		EventCategory eventCategory2 = createEventCategory("event_category2");
		em.persist(eventCategory1);
		em.persist(eventCategory2);

		//save Event
		Event event1 = createEvent(null, "event1", now.minusDays(2), now.plusDays(1), "");
		Event event2 = createEvent(null, "event2", now.minusDays(2), now.plusDays(1), "");
		Event event3 = createEvent(null, "event3", now.plusDays(1), now.plusDays(1), "");
		Event event4 = createEvent(null, "event4", now.plusDays(1), now.plusDays(1), "");
		em.persist(event1);
		em.persist(event2);
		em.persist(event3);
		em.persist(event4);

		// Artist, EventCategory, EventImage -> event
		EventArtist eventArtist1 = createEventArtist(event1, artist1);
		EventArtist eventArtist2 = createEventArtist(event2, artist1);
		EventArtist eventArtist3 = createEventArtist(event2, artist2);
		EventArtist eventArtist4 = createEventArtist(event3, artist2);
		EventArtist eventArtist5 = createEventArtist(event4, artist1);
		em.persist(eventArtist1);
		em.persist(eventArtist2);
		em.persist(eventArtist3);
		em.persist(eventArtist4);
		em.persist(eventArtist5);
		EventInfoCategory eventInfoCategory1 = createEventInfoCategory(event1, eventCategory1);
		EventInfoCategory eventInfoCategory2 = createEventInfoCategory(event2, eventCategory2);
		EventInfoCategory eventInfoCategory3 = createEventInfoCategory(event3, eventCategory2);
		EventInfoCategory eventInfoCategory4 = createEventInfoCategory(event4, eventCategory1);
		em.persist(eventInfoCategory1);
		em.persist(eventInfoCategory2);
		em.persist(eventInfoCategory3);
		em.persist(eventInfoCategory4);
		EventImage image1 = createEventImage(event1, "image1", false);
		EventImage image2 = createEventImage(event1, "image2", true);
		EventImage image3 = createEventImage(event2, "image3", true);
		EventImage image4 = createEventImage(event2, "image4", false);
		EventImage image5 = createEventImage(event3, "image5", true);
		EventImage image6 = createEventImage(event4, "image6", true);
		em.persist(image1);
		em.persist(image2);
		em.persist(image3);
		em.persist(image4);
		em.persist(image5);
		em.persist(image6);

		// save EventLike
		EventLike eventLike1 = createEventLike(event1, member1);
		EventLike eventLike2 = createEventLike(event3, member2);
		EventLike eventLike3 = createEventLike(event4, member1);
		EventLike eventLike4 = createEventLike(event2, member2);
		EventLike eventLike5 = createEventLike(event3, member1);
		em.persist(eventLike1);
		em.persist(eventLike2);
		em.persist(eventLike3);
		em.persist(eventLike4);
		em.persist(eventLike5);

		em.flush();
		em.clear();

		Long searchMemberId = member1.getId();
		Pageable pageable = PageRequest.of(0, 3);

		MyLikeEventsServiceReq request = MyLikeEventsServiceReq.builder()
			.memberId(searchMemberId)
			.date(now)
			.pageable(pageable)
			.build();

		//when
		Page<EventsRes> myLikeEventsResList = eventService.getMyLikeEventsRes(request);

		//then
		assertThat(myLikeEventsResList).hasSize(3)
			.extracting("storeName", "likeId")
			.containsExactly(
				Tuple.tuple("event1", eventLike1.getId()),
				Tuple.tuple("event3", eventLike5.getId()),
				Tuple.tuple("event4", eventLike3.getId()));
	}

	@DisplayName("좋아요, 리뷰 수로 정렬하여 EventsMapRes 목록 조회한다")
	@Test
	void getEventsForMap() throws Exception {
		//given
		LocalDate now = LocalDate.now();

		Event event1 = createEvent(null, "event1", now.minusDays(2), now.minusDays(1), null);
		Event event2 = createEvent(null, "event2", now.minusDays(2), now, null);
		Event event3 = createEvent(null, "event3", now.minusDays(1), now, null);
		Event event4 = createEvent(null, "event4", now.minusDays(1), now.plusDays(1), null);
		Event event5 = createEvent(null, "event5", now.minusDays(1), now.plusDays(2), null);
		Event event6 = createEvent(null, "event6", now, now.plusDays(2), null);
		eventRepository.saveAll(List.of(event1, event2, event3, event4, event5, event6));

		Artist artist1 = createArtist("artist1", null);
		Artist artist2 = createArtist("artist2", null);
		em.persist(artist1);
		em.persist(artist2);

		EventArtist eventArtist1 = createEventArtist(event1, artist1);
		EventArtist eventArtist2 = createEventArtist(event2, artist1);
		EventArtist eventArtist3 = createEventArtist(event3, artist2);
		EventArtist eventArtist4 = createEventArtist(event4, artist2);
		EventArtist eventArtist5 = createEventArtist(event5, artist1);
		EventArtist eventArtist6 = createEventArtist(event6, null);
		em.persist(eventArtist1);
		em.persist(eventArtist2);
		em.persist(eventArtist3);
		em.persist(eventArtist4);
		em.persist(eventArtist5);
		em.persist(eventArtist6);

		for (int i = 0; i < 2; i++) {
			em.persist(createEventLike(event1, null));
		}
		for (int i = 0; i < 8; i++) {
			em.persist(createEventLike(event2, null));
		}
		for (int i = 0; i < 4; i++) {
			em.persist(createEventLike(event3, null));
		}
		for (int i = 0; i < 10; i++) {
			em.persist(createEventLike(event4, null));
		}
		for (int i = 0; i < 6; i++) {
			em.persist(createEventLike(event5, null));
		}
		for (int i = 0; i < 12; i++) {
			em.persist(createEventLike(event6, null));
		}

		for (int i = 0; i < 11; i++) {
			em.persist(createReview(event1));
		}
		for (int i = 0; i < 5; i++) {
			em.persist(createReview(event2));
		}
		for (int i = 0; i < 9; i++) {
			em.persist(createReview(event3));
		}
		for (int i = 0; i < 3; i++) {
			em.persist(createReview(event4));
		}
		for (int i = 0; i < 7; i++) {
			em.persist(createReview(event5));
		}
		em.persist(createReview(event6));

		em.flush();
		em.clear();

		Pageable pageable = PageRequest.of(1, 2, Sort.Direction.DESC, "likeCount");
		//when
		Page<EventsMapRes> events = eventService.getEventsForMap(now, pageable);

		//then
		assertThat(events).hasSize(2)
			.extracting("storeName", "likeCount", "reviewCount")
			.containsExactly(
				Tuple.tuple("event5", 6L, 7L),
				Tuple.tuple("event3", 4L, 9L));
	}

	@DisplayName("날짜 기준 진행중인 Event의 HashtagsRes 목록 조회")
	@Test
	void getHashtagResListByDate() throws Exception {
		//given
		LocalDate date = LocalDate.now();

		Event event1 = createEvent(null, "event1", date.minusDays(10), date.minusDays(6), "#hashtag1");
		Event event2 = createEvent(null, "event2", date.minusDays(1), date, "#hashtag2");
		Event event3 = createEvent(null, "event3", date, date.plusDays(1), "#hashtag3");
		Event event4 = createEvent(null, "event4", date, date.plusDays(4), "#hashtag4");
		em.persist(event1);
		em.persist(event2);
		em.persist(event3);
		em.persist(event4);

		Artist artist1 = Artist.builder().build();
		Artist artist2 = Artist.builder().build();
		em.persist(artist1);
		em.persist(artist2);

		EventArtist eventArtist1 = createEventArtist(event1, artist1);
		EventArtist eventArtist2 = createEventArtist(event1, artist2);
		EventArtist eventArtist3 = createEventArtist(event2, artist1);
		EventArtist eventArtist4 = createEventArtist(event2, artist2);
		EventArtist eventArtist5 = createEventArtist(event3, artist2);
		EventArtist eventArtist6 = createEventArtist(event4, null);
		em.persist(eventArtist1);
		em.persist(eventArtist2);
		em.persist(eventArtist3);
		em.persist(eventArtist4);
		em.persist(eventArtist5);
		em.persist(eventArtist6);

		//when
		List<HashtagRes> hashtagResList = eventService.getHashtagResListByDate(date);

		//then
		assertThat(hashtagResList).hasSize(2)
			.extracting("eventId", "hashtag")
			.containsExactly(
				Tuple.tuple(event2.getId(), event2.getHashtag()),
				Tuple.tuple(event3.getId(), event3.getHashtag()));
	}

	@DisplayName("이벤트를 수정한다")
	@Test
	void updateEvent() throws Exception {
		//given
		Member member1 = createMember("member1");
		em.persist(member1);

		Event event1 = createEvent(member1, "event1", null, null, null);
		em.persist(event1);

		Artist artist1 = createArtist("artist1", null);
		Artist artist2 = createArtist("artist2", null);
		Artist artist3 = createArtist("artist3", null);
		em.persist(artist1);
		em.persist(artist2);
		em.persist(artist3);

		EventArtist eventArtist1 = createEventArtist(event1, artist1);
		EventArtist eventArtist2 = createEventArtist(event1, artist2);
		em.persist(eventArtist1);
		em.persist(eventArtist2);

		EventCategory category1 = createEventCategory("category1");
		EventCategory category2 = createEventCategory("category2");
		EventCategory category3 = createEventCategory("category3");
		EventCategory category4 = createEventCategory("category4");
		em.persist(category1);
		em.persist(category2);
		em.persist(category3);
		em.persist(category4);

		EventInfoCategory eventInfoCategory1 = createEventInfoCategory(event1, category1);
		EventInfoCategory eventInfoCategory2 = createEventInfoCategory(event1, category3);
		em.persist(eventInfoCategory1);
		em.persist(eventInfoCategory2);

		EventImage image1 = createEventImage(event1, "image1", false);
		em.persist(image1);

		Long memberId = member1.getId();

		em.flush();
		em.clear();

		Long updateEventId = event1.getId();
		LocalDate now = LocalDate.now();

		UpdateEventServiceReq request = UpdateEventServiceReq.builder()
			.id(updateEventId)
			.storeName("new_storeName")
			.fromDate(now)
			.toDate(now.plusDays(1))
			.address("new_address")
			.businessHour("new_businessHour")
			.hashtag("new_hashtag")
			.twitterUrl("new_twitterUrl")
			.artistIds(List.of(artist2.getId(), artist3.getId()))
			.categoryIds(List.of(category2.getId(), category4.getId(), category1.getId()))
			.imageFilenames(List.of("new_filename1", "new_filename2"))
			.build();

		when(artistService.getArtistsByIds(any())).thenReturn(List.of(artist2, artist3));
		when(eventCategoryService.getEventCategoriesByIds(any())).thenReturn(List.of(category2, category4, category1));

		//when
		eventService.updateEvent(memberId, request);

		em.flush();
		em.clear();

		//then
		Event findEvent = eventRepository.findById(updateEventId).get();
		assertThat(findEvent).extracting("storeName", "fromDate", "toDate", "address", "hashtag")
			.containsOnly("new_storeName", now, now.plusDays(1), "new_address", "new_hashtag");

		assertThat(findEvent.getEventArtists()).hasSize(2)
			.extracting("artist.id")
			.containsExactlyInAnyOrder(artist2.getId(), artist3.getId());

		assertThat(findEvent.getEventInfoCategories()).hasSize(3)
			.extracting("eventCategory.id")
			.containsExactlyInAnyOrder(category2.getId(), category4.getId(), category1.getId());

		assertThat(findEvent.getEventImages()).hasSize(2)
			.extracting("image", "thumbnail")
			.containsExactlyInAnyOrder(Tuple.tuple("new_filename1", true), Tuple.tuple("new_filename2", false));
	}

	@DisplayName("이벤트를 삭제한다")
	@Test
	void deleteEvent() throws Exception {
		//given
		Member member1 = createMember("member1");
		em.persist(member1);

		Event event1 = createEvent(member1, "event1", null, null, null);
		em.persist(event1);

		Artist artist1 = createArtist("artist1", null);
		Artist artist2 = createArtist("artist2", null);
		Artist artist3 = createArtist("artist3", null);
		em.persist(artist1);
		em.persist(artist2);
		em.persist(artist3);

		EventArtist eventArtist1 = createEventArtist(event1, artist1);
		EventArtist eventArtist2 = createEventArtist(event1, artist2);
		em.persist(eventArtist1);
		em.persist(eventArtist2);

		EventCategory category1 = createEventCategory("category1");
		EventCategory category2 = createEventCategory("category2");
		EventCategory category3 = createEventCategory("category3");
		EventCategory category4 = createEventCategory("category4");
		em.persist(category1);
		em.persist(category2);
		em.persist(category3);
		em.persist(category4);

		EventInfoCategory eventInfoCategory1 = createEventInfoCategory(event1, category1);
		EventInfoCategory eventInfoCategory2 = createEventInfoCategory(event1, category3);
		em.persist(eventInfoCategory1);
		em.persist(eventInfoCategory2);

		EventImage eventImage1 = createEventImage(event1, null, false);
		EventImage eventImage2 = createEventImage(event1, null, false);
		em.persist(eventImage1);
		em.persist(eventImage2);

		EventBookmark eventBookmark1 = createEventBookmark(event1, null);
		EventBookmark eventBookmark2 = createEventBookmark(event1, null);
		em.persist(eventBookmark1);
		em.persist(eventBookmark2);

		EventLike eventLike1 = createEventLike(event1, null);
		EventLike eventLike2 = createEventLike(event1, null);
		EventLike eventLike3 = createEventLike(event1, null);
		em.persist(eventLike1);
		em.persist(eventLike2);
		em.persist(eventLike3);

		em.flush();
		em.clear();

		Long deleteEventId = event1.getId();

		//when
		eventService.deleteEvent(member1.getId(), deleteEventId);

		em.flush();
		em.clear();

		//then
		Optional<Event> findEvent = eventRepository.findById(deleteEventId);
		assertThat(findEvent).isEmpty();

		List<EventInfoCategory> findEventInfoCategories = eventInfoCategoryRepository.findAllById(List.of(
			eventInfoCategory1.getId(), eventInfoCategory2.getId()
		));
		assertThat(findEventInfoCategories).isEmpty();
		List<EventLike> findEventLikes = eventLikeRepository.findAllById(
			List.of(eventLike1.getId(), eventLike2.getId(), eventLike3.getId()));
		assertThat(findEventLikes).isEmpty();
	}

	Event createEvent(Member member, String storeName, LocalDate fromDate, LocalDate toDate, String hashtag) {
		return Event.builder()
			.member(member)
			.storeName(storeName)
			.fromDate(fromDate)
			.toDate(toDate)
			.hashtag(hashtag)
			.eventImages(List.of())
			.build();
	}

	Review createReview(Event event) {
		return Review.builder()
			.event(event)
			.build();
	}

	ArtistType createArtistType() {
		return ArtistType.builder().type("artist_type").build();
	}

	Artist createArtist(String name, ArtistType type) {
		return Artist.builder().name(name).artistType(type).build();
	}

	EventArtist createEventArtist(Event event, Artist artist) {
		return EventArtist.builder().event(event).artist(artist).build();
	}

	EventCategory createEventCategory(String category) {
		return EventCategory.builder().category(category).build();
	}

	EventInfoCategory createEventInfoCategory(Event event, EventCategory category) {
		return EventInfoCategory.builder().event(event).eventCategory(category).build();
	}

	EventImage createEventImage(Event event, String image, boolean thumbnail) {
		return EventImage.builder()
			.event(event)
			.image(image)
			.thumbnail(thumbnail)
			.build();
	}

	EventLike createEventLike(Event event, Member member) {
		return EventLike.builder()
			.event(event)
			.member(member)
			.build();
	}

	EventBookmark createEventBookmark(Event event, Member member) {
		return EventBookmark.builder()
			.event(event)
			.member(member)
			.build();
	}

	Member createMember(String email) {
		return Member.builder()
			.email(email)
			.username("member_username")
			.password("member_password")
			.role(Role.USER)
			.build();
	}

	LastSearchArtist createLastSearchArtist(Member member, Artist artist) {
		return LastSearchArtist.builder()
			.member(member)
			.artist(artist)
			.build();
	}

	@DisplayName("Artist id, date로 EventsRes 목록 조회")
	@Nested
	class GetEventsResList {
		@DisplayName("로그인한 Member가 없는 경우")
		@Test
		void getEventsResList1() throws Exception {
			//given
			LocalDate now = LocalDate.now();

			// save Artist
			ArtistType artistType = createArtistType();
			em.persist(artistType);
			Artist artist1 = createArtist("artist1", artistType);
			Artist artist2 = createArtist("artist2", artistType);
			em.persist(artist1);
			em.persist(artist2);

			// save EventCategory
			EventCategory eventCategory1 = createEventCategory("event_category1");
			EventCategory eventCategory2 = createEventCategory("event_category2");
			em.persist(eventCategory1);
			em.persist(eventCategory2);

			//save Event
			Event event1 = createEvent(null, "event1", now.minusDays(2), now.plusDays(1), "");
			Event event2 = createEvent(null, "event2", now.plusDays(1), now.plusDays(1), "");
			em.persist(event1);
			em.persist(event2);

			// Artist, EventCategory, EventImage -> event
			EventArtist eventArtist1 = createEventArtist(event1, artist1);
			EventArtist eventArtist2 = createEventArtist(event2, artist1);
			EventArtist eventArtist3 = createEventArtist(event2, artist2);
			em.persist(eventArtist1);
			em.persist(eventArtist2);
			em.persist(eventArtist3);
			EventInfoCategory eventInfoCategory1 = createEventInfoCategory(event1, eventCategory1);
			EventInfoCategory eventInfoCategory2 = createEventInfoCategory(event2, eventCategory2);
			em.persist(eventInfoCategory1);
			em.persist(eventInfoCategory2);
			EventImage image1 = createEventImage(event1, "image1", false);
			EventImage image2 = createEventImage(event1, "image2", true);
			EventImage image3 = createEventImage(event2, "image3", true);
			EventImage image4 = createEventImage(event2, "image4", false);
			em.persist(image1);
			em.persist(image2);
			em.persist(image3);
			em.persist(image4);

			em.flush();
			em.clear();

			Artist searchArtist = artist1;

			Pageable pageable = PageRequest.of(0, 3);

			EventSearchServiceReq request = EventSearchServiceReq.builder()
				.member(Optional.empty())
				.date(now)
				.artistId(searchArtist.getId())
				// .onlyInProgress(false) default == false
				.pageable(pageable)
				.build();

			when(artistService.getArtist(any())).thenReturn(searchArtist);

			//when
			Page<EventsRes> eventsResList = eventService.getEventsResList(request);

			//then
			assertThat(eventsResList).hasSize(2)
				.extracting("storeName", "likeId", "bookmarkId")
				.containsExactly(
					Tuple.tuple("event1", null, null),
					Tuple.tuple("event2", null, null));
		}

		@DisplayName("로그인한 Member가 있는 경우")
		@Test
		void getEventsResList2() throws Exception {
			//given
			LocalDate now = LocalDate.now();

			Member member1 = createMember("member1");
			em.persist(member1);

			// save Artist
			ArtistType artistType = createArtistType();
			em.persist(artistType);
			Artist artist1 = createArtist("artist1", artistType);
			Artist artist2 = createArtist("artist2", artistType);
			em.persist(artist1);
			em.persist(artist2);

			// save LastSearchArtist
			LastSearchArtist lastSearchArtist1 = createLastSearchArtist(member1, artist2);
			em.persist(lastSearchArtist1);

			// save EventCategory
			EventCategory eventCategory1 = createEventCategory("event_category1");
			EventCategory eventCategory2 = createEventCategory("event_category2");
			em.persist(eventCategory1);
			em.persist(eventCategory2);

			//save Event
			Event event1 = createEvent(null, "event1", now.minusDays(2), now.plusDays(1), "");
			Event event2 = createEvent(null, "event2", now.plusDays(1), now.plusDays(1), "");
			em.persist(event1);
			em.persist(event2);

			// Artist, EventCategory, EventImage -> event
			EventArtist eventArtist1 = createEventArtist(event1, artist1);
			EventArtist eventArtist2 = createEventArtist(event2, artist1);
			EventArtist eventArtist3 = createEventArtist(event2, artist2);
			em.persist(eventArtist1);
			em.persist(eventArtist2);
			em.persist(eventArtist3);
			EventInfoCategory eventInfoCategory1 = createEventInfoCategory(event1, eventCategory1);
			EventInfoCategory eventInfoCategory2 = createEventInfoCategory(event2, eventCategory2);
			em.persist(eventInfoCategory1);
			em.persist(eventInfoCategory2);
			EventImage image1 = createEventImage(event1, "image1", false);
			EventImage image2 = createEventImage(event1, "image2", true);
			EventImage image3 = createEventImage(event2, "image3", true);
			EventImage image4 = createEventImage(event2, "image4", false);
			em.persist(image1);
			em.persist(image2);
			em.persist(image3);
			em.persist(image4);

			em.flush();
			em.clear();

			Artist searchArtist = artist1;

			Pageable pageable = PageRequest.of(0, 3);

			EventSearchServiceReq request = EventSearchServiceReq.builder()
				.member(Optional.of(member1))
				.date(now)
				.artistId(searchArtist.getId())
				// .onlyInProgress(false) default == false
				.pageable(pageable)
				.build();

			when(artistService.getArtist(any())).thenReturn(searchArtist);

			//when
			Page<EventsRes> eventsResList = eventService.getEventsResList(request);

			//then
			assertThat(eventsResList).hasSize(2)
				.extracting("storeName", "likeId", "bookmarkId")
				.containsExactly(
					Tuple.tuple("event1", null, null),
					Tuple.tuple("event2", null, null));

			Optional<LastSearchArtist> findLastSearch = lastSearchArtistRepository.findByMember(member1);
			assertThat(findLastSearch).isNotEmpty();
			assertThat(findLastSearch.get()).extracting("member.email", "artist.name")
				.containsExactly("member1", "artist1");

		}
	}
}
