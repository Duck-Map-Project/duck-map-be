package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventLike;
import com.teamddd.duckmap.entity.User;

@SpringBootTest
@Transactional
public class EventLikeRepositoryTest {
	@Autowired
	EventLikeRepository eventLikeRepository;
	@Autowired
	EntityManager em;

	@Test
	void countByEventId() throws Exception {
		//given
		User user1 = User.builder().build();
		User user2 = User.builder().build();
		User user3 = User.builder().build();
		em.persist(user1);
		em.persist(user2);
		em.persist(user3);

		Event event1 = createEvent(user1, "event1");
		Event event2 = createEvent(user1, "event2");
		Event event3 = createEvent(user1, "event3");
		em.persist(event1);
		em.persist(event2);
		em.persist(event3);

		EventLike eventLike1 = createEventLike(user1, event1);
		EventLike eventLike2 = createEventLike(user1, event2);
		EventLike eventLike3 = createEventLike(user2, event1);
		EventLike eventLike4 = createEventLike(user2, event3);
		EventLike eventLike5 = createEventLike(user3, event1);

		em.persist(eventLike1);
		em.persist(eventLike2);
		em.persist(eventLike3);
		em.persist(eventLike4);
		em.persist(eventLike5);

		//when
		Integer likeCount1 = eventLikeRepository.countByEventId(event1.getId());
		Integer likeCount2 = eventLikeRepository.countByEventId(event2.getId());
		Integer likeCount3 = eventLikeRepository.countByEventId(event3.getId());
		//then
		assertThat(likeCount1).isEqualTo(3);
		assertThat(likeCount2).isEqualTo(1);
		assertThat(likeCount3).isEqualTo(1);

	}

	private Event createEvent(User user, String storeName) {
		return Event.builder().user(user).storeName(storeName).build();
	}

	private EventLike createEventLike(User user, Event event) {
		return EventLike.builder().user(user).event(event).build();
	}
}
