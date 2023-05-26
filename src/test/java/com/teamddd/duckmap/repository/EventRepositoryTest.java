package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.event.EventLikeBookmarkDto;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventArtist;
import com.teamddd.duckmap.entity.EventBookmark;
import com.teamddd.duckmap.entity.EventLike;
import com.teamddd.duckmap.entity.User;

@Transactional
@SpringBootTest
class EventRepositoryTest {
	@Autowired
	EventRepository eventRepository;
	@Autowired
	EntityManager em;

	@DisplayName("user pk로 event 목록 검색, EventLike, EventBookmark와 join하여 조회")
	@Test
	void findMyEvents() throws Exception {
		//given
		User user = User.builder().build();
		User user2 = User.builder().build();
		em.persist(user);
		em.persist(user2);

		Event event1 = createEvent(user, "event1", LocalDate.now(), LocalDate.now(), "#hashtag");
		Event event2 = createEvent(user2, "event2", LocalDate.now(), LocalDate.now(), "#hashtag");
		Event event3 = createEvent(user, "event3", LocalDate.now(), LocalDate.now(), "#hashtag");
		em.persist(event1);
		em.persist(event2);
		em.persist(event3);

		EventLike eventLike1 = createEventLike(user, event1);
		EventLike eventLike2 = createEventLike(user, event2);
		EventBookmark eventBookmark2 = createEventBookmark(user, event2);
		EventBookmark eventBookmark3 = createEventBookmark(user, event3);
		em.persist(eventLike1);
		em.persist(eventLike2);
		em.persist(eventBookmark2);
		em.persist(eventBookmark3);

		PageRequest pageRequest = PageRequest.of(0, 2);

		//when
		Page<EventLikeBookmarkDto> myEvents = eventRepository.findMyEvents(user.getId(), pageRequest);

		//then
		assertThat(myEvents).hasSize(2)
			.extracting("event.user.id", "event.storeName", "like.id", "bookmark.id")
			.containsExactly(Tuple.tuple(user.getId(), "event1", eventLike1.getId(), null),
				Tuple.tuple(user.getId(), "event3", null, eventBookmark3.getId()));

		assertThat(myEvents.getTotalElements()).isEqualTo(2);
		assertThat(myEvents.getTotalPages()).isEqualTo(1);
	}

	private Event createEvent(User user, String storeName, LocalDate fromDate, LocalDate toDate, String hashtag) {
		return Event.builder()
			.user(user)
			.storeName(storeName)
			.fromDate(fromDate)
			.toDate(toDate)
			.hashtag(hashtag)
			.build();
	}

	private EventLike createEventLike(User user, Event event) {
		return EventLike.builder().user(user).event(event).build();
	}

	private EventBookmark createEventBookmark(User user, Event event) {
		return EventBookmark.builder().user(user).event(event).build();
	}

	private EventArtist createEventArtist(Event event1, Artist artist1) {
		return EventArtist.builder()
			.event(event1)
			.artist(artist1)
			.build();
	}

	@Nested
	@DisplayName("EventArtist pk, fromDate<=LocalDate<=toDate인 event 목록 EventLike, EventBookmark와 join하여 조회")
	class FindByArtistAndDate {

		@DisplayName("EventArtist pk로 조회")
		@Test
		void findByArtistAndDate1() throws Exception {
			//given
			LocalDate date = LocalDate.now();

			User user = User.builder().build();
			em.persist(user);

			Event event1 = createEvent(user, "event1", date.minusDays(10), date.minusDays(6), "#hashtag");
			Event event2 = createEvent(user, "event2", date.minusDays(1), date, "#hashtag");
			Event event3 = createEvent(user, "event3", date, date.plusDays(1), "#hashtag");
			Event event4 = createEvent(user, "event4", date.plusDays(2), date.plusDays(4), "#hashtag");
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
			EventArtist eventArtist4 = createEventArtist(event3, artist2);
			EventArtist eventArtist5 = createEventArtist(event4, artist1);
			em.persist(eventArtist1);
			em.persist(eventArtist2);
			em.persist(eventArtist3);
			em.persist(eventArtist4);
			em.persist(eventArtist5);

			EventLike eventLike1 = createEventLike(user, event1);
			EventLike eventLike2 = createEventLike(user, event2);
			EventBookmark eventBookmark2 = createEventBookmark(user, event2);
			EventBookmark eventBookmark3 = createEventBookmark(user, event3);
			em.persist(eventLike1);
			em.persist(eventLike2);
			em.persist(eventBookmark2);
			em.persist(eventBookmark3);

			PageRequest pageRequest = PageRequest.of(0, 2);

			//when
			Page<EventLikeBookmarkDto> events = eventRepository.findByArtistAndDate(artist1.getId(), null, null,
				pageRequest);

			//then
			assertThat(events).hasSize(2)
				.extracting("event.storeName", "like.id", "bookmark.id")
				.containsExactly(Tuple.tuple("event1", null, null),
					Tuple.tuple("event2", null, null));

			assertThat(events.getTotalElements()).isEqualTo(3);
			assertThat(events.getTotalPages()).isEqualTo(2);
		}

		@DisplayName("fromDate<=LocalDate<=toDate로 조회")
		@Test
		void findByArtistAndDate2() throws Exception {
			//given
			LocalDate date = LocalDate.now();

			User user = User.builder().build();
			em.persist(user);

			Event event1 = createEvent(user, "event1", date.minusDays(10), date, "#hashtag");
			Event event2 = createEvent(user, "event2", date.minusDays(1), date, "#hashtag");
			Event event3 = createEvent(user, "event3", date, date.plusDays(1), "#hashtag");
			Event event4 = createEvent(user, "event4", date.plusDays(2), date.plusDays(4), "#hashtag");
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
			EventArtist eventArtist4 = createEventArtist(event3, artist2);
			EventArtist eventArtist5 = createEventArtist(event4, artist1);
			em.persist(eventArtist1);
			em.persist(eventArtist2);
			em.persist(eventArtist3);
			em.persist(eventArtist4);
			em.persist(eventArtist5);

			EventLike eventLike1 = createEventLike(user, event1);
			EventLike eventLike2 = createEventLike(user, event2);
			EventBookmark eventBookmark2 = createEventBookmark(user, event2);
			EventBookmark eventBookmark3 = createEventBookmark(user, event3);
			em.persist(eventLike1);
			em.persist(eventLike2);
			em.persist(eventBookmark2);
			em.persist(eventBookmark3);

			PageRequest pageRequest = PageRequest.of(0, 2);

			//when
			Page<EventLikeBookmarkDto> events = eventRepository.findByArtistAndDate(null, date, null,
				pageRequest);

			//then
			assertThat(events).hasSize(2)
				.extracting("event.storeName", "like.id", "bookmark.id")
				.containsExactly(Tuple.tuple("event1", null, null),
					Tuple.tuple("event2", null, null));

			assertThat(events.getTotalElements()).isEqualTo(3);
			assertThat(events.getTotalPages()).isEqualTo(2);
		}

		@DisplayName("user pk로 EventLike, EventBookmark와 join하여 조회")
		@Test
		void findByArtistAndDate3() throws Exception {
			//given
			LocalDate date = LocalDate.now();

			User user = User.builder().build();
			em.persist(user);

			Event event1 = createEvent(user, "event1", date.minusDays(10), date.minusDays(6), "#hashtag");
			Event event2 = createEvent(user, "event2", date.minusDays(1), date, "#hashtag");
			Event event3 = createEvent(user, "event3", date, date.plusDays(1), "#hashtag");
			Event event4 = createEvent(user, "event4", date.plusDays(2), date.plusDays(4), "#hashtag");
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
			EventArtist eventArtist4 = createEventArtist(event3, artist2);
			EventArtist eventArtist5 = createEventArtist(event4, artist1);
			em.persist(eventArtist1);
			em.persist(eventArtist2);
			em.persist(eventArtist3);
			em.persist(eventArtist4);
			em.persist(eventArtist5);

			EventLike eventLike1 = createEventLike(user, event1);
			EventLike eventLike2 = createEventLike(user, event2);
			EventBookmark eventBookmark2 = createEventBookmark(user, event2);
			EventBookmark eventBookmark3 = createEventBookmark(user, event3);
			em.persist(eventLike1);
			em.persist(eventLike2);
			em.persist(eventBookmark2);
			em.persist(eventBookmark3);

			PageRequest pageRequest = PageRequest.of(0, 3);

			//when
			Page<EventLikeBookmarkDto> events = eventRepository.findByArtistAndDate(null, null, user.getId(),
				pageRequest);

			//then
			assertThat(events).hasSize(3)
				.extracting("event.storeName", "like.id", "bookmark.id")
				.containsExactly(Tuple.tuple("event1", eventLike1.getId(), null),
					Tuple.tuple("event2", eventLike2.getId(), eventBookmark2.getId()),
					Tuple.tuple("event3", null, eventBookmark3.getId()));

			assertThat(events.getTotalElements()).isEqualTo(4);
			assertThat(events.getTotalPages()).isEqualTo(2);
		}

	}
}
