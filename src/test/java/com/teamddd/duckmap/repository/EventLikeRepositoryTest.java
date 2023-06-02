package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventLike;
import com.teamddd.duckmap.entity.Member;

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
		Member member1 = Member.builder().build();
		Member member2 = Member.builder().build();
		Member member3 = Member.builder().build();
		em.persist(member1);
		em.persist(member2);
		em.persist(member3);

		Event event1 = createEvent(member1, "event1");
		Event event2 = createEvent(member1, "event2");
		Event event3 = createEvent(member1, "event3");
		em.persist(event1);
		em.persist(event2);
		em.persist(event3);

		EventLike eventLike1 = createEventLike(member1, event1);
		EventLike eventLike2 = createEventLike(member1, event2);
		EventLike eventLike3 = createEventLike(member2, event1);
		EventLike eventLike4 = createEventLike(member2, event3);
		EventLike eventLike5 = createEventLike(member3, event1);

		em.persist(eventLike1);
		em.persist(eventLike2);
		em.persist(eventLike3);
		em.persist(eventLike4);
		em.persist(eventLike5);

		//when
		Long likeCount1 = eventLikeRepository.countByEventId(event1.getId());
		Long likeCount2 = eventLikeRepository.countByEventId(event2.getId());
		Long likeCount3 = eventLikeRepository.countByEventId(event3.getId());
		//then
		assertThat(likeCount1).isEqualTo(3);
		assertThat(likeCount2).isEqualTo(1);
		assertThat(likeCount3).isEqualTo(1);

	}

	private Event createEvent(Member member, String storeName) {
		return Event.builder().member(member).storeName(storeName).build();
	}

	private EventLike createEventLike(Member member, Event event) {
		return EventLike.builder().member(member).event(event).build();
	}
}
