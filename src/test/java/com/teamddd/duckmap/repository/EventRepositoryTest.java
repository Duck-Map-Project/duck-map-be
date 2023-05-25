package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.event.EventLikeBookmarkDto;
import com.teamddd.duckmap.entity.Event;
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
}
