package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.event.CreateEventReq;
import com.teamddd.duckmap.dto.event.event.EventRes;
import com.teamddd.duckmap.dto.event.event.EventSearchServiceReq;
import com.teamddd.duckmap.dto.event.event.EventsRes;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.ArtistType;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventArtist;
import com.teamddd.duckmap.entity.EventCategory;
import com.teamddd.duckmap.entity.EventImage;
import com.teamddd.duckmap.entity.EventInfoCategory;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.repository.EventLikeRepository;
import com.teamddd.duckmap.repository.EventRepository;
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
	@MockBean
	EventLikeRepository eventLikeRepository;

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

	@DisplayName("Artist id, date로 EventsRes 목록 조회")
	@Test
	void getEventsResList() throws Exception {
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
			.memberId(null)
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
}
