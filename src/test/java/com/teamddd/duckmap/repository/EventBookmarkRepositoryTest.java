package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventBookmark;
import com.teamddd.duckmap.entity.Member;

@SpringBootTest
@Transactional
class EventBookmarkRepositoryTest {
	@Autowired
	EventBookmarkRepository eventBookmarkRepository;
	@Autowired
	EntityManager em;

	@DisplayName("Event fk로 EventBookmark 목록을 삭제한다")
	@Test
	void deleteById() throws Exception {
		//given
		Event event1 = createEvent(null, "event1");
		em.persist(event1);

		EventBookmark eventBookmark1 = createEventBookmark(null, null);
		EventBookmark eventBookmark2 = createEventBookmark(null, event1);
		EventBookmark eventBookmark3 = createEventBookmark(null, event1);
		em.persist(eventBookmark1);
		em.persist(eventBookmark2);
		em.persist(eventBookmark3);

		em.flush();
		em.clear();

		//when
		int count = eventBookmarkRepository.deleteByEventId(event1.getId());

		em.flush();
		em.clear();

		//then
		assertThat(count).isEqualTo(2);

		List<EventBookmark> findEventBookmarks = eventBookmarkRepository.findAll();
		assertThat(findEventBookmarks).hasSize(1)
			.extracting("id")
			.containsExactly(eventBookmark1.getId());
	}

	private Event createEvent(Member member, String storeName) {
		return Event.builder().member(member).storeName(storeName).build();
	}

	private EventBookmark createEventBookmark(Member member, Event event) {
		return EventBookmark.builder().member(member).event(event).build();
	}
}
