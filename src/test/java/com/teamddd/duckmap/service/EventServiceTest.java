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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.event.CreateEventReq;
import com.teamddd.duckmap.dto.event.event.EventRes;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.ArtistType;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventArtist;
import com.teamddd.duckmap.entity.EventCategory;
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
		CreateEventReq request = new CreateEventReq();
		ReflectionTestUtils.setField(request, "storeName", "store name");
		ReflectionTestUtils.setField(request, "fromDate", LocalDate.now());
		ReflectionTestUtils.setField(request, "toDate", LocalDate.now());
		ReflectionTestUtils.setField(request, "address", "address");
		ReflectionTestUtils.setField(request, "artistIds", List.of(1L));
		ReflectionTestUtils.setField(request, "categoryIds", List.of(1L));
		ReflectionTestUtils.setField(request, "imageFilenames", List.of("filename"));

		Member member = Member.builder().username("member1").build();

		List<Artist> artists = List.of();
		List<EventCategory> categories = List.of();
		when(artistService.getArtistsByIds(any())).thenReturn(artists);
		when(eventCategoryService.getEventCategoriesByIds(any())).thenReturn(categories);

		//when
		Long eventId = eventService.createEvent(request, member);

		//then
		assertThat(eventId).isNotNull();

		Optional<Event> findEvent = eventRepository.findById(eventId);
		assertThat(findEvent).isNotEmpty();
		assertThat(findEvent.get()).extracting("storeName", "member.username").containsOnly("store name", "member1");
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
		assertThat(eventRes).extracting("id", "storeName", "inProgress", "like", "bookmark", "score", "likeCount")
			.containsOnly(eventId, "event1", true, false, false, 4.4, 12);

		assertThat(eventRes.getArtists()).extracting("name", "artistType.type")
			.containsExactlyInAnyOrder(Tuple.tuple("artist1", "artist_type"), Tuple.tuple("artist2", "artist_type"));
		assertThat(eventRes.getCategories()).extracting("category")
			.containsExactlyInAnyOrder("event_category1", "event_category2");
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
}
