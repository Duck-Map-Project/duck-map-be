package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventCategory;
import com.teamddd.duckmap.entity.EventInfoCategory;

@Transactional
@SpringBootTest
class EventInfoCategoryRepositoryTest {
	@Autowired
	EventInfoCategoryRepository eventInfoCategoryRepository;
	@Autowired
	EntityManager em;

	@DisplayName("EventCategory로 EventInfoCategory 수 조회")
	@Test
	void countByEventCategory() throws Exception {
		//given
		Event event1 = createEvent("event1");
		Event event2 = createEvent("event2");
		Event event3 = createEvent("event3");
		em.persist(event1);
		em.persist(event2);
		em.persist(event3);

		EventCategory category1 = createEventCategory("category1");
		EventCategory category2 = createEventCategory("category2");
		em.persist(category1);
		em.persist(category2);

		EventInfoCategory eventInfoCategory1 = createEventInfoCategory(event1, category1);
		EventInfoCategory eventInfoCategory2 = createEventInfoCategory(event2, category2);
		EventInfoCategory eventInfoCategory3 = createEventInfoCategory(event3, category1);
		EventInfoCategory eventInfoCategory4 = createEventInfoCategory(event1, category2);
		EventInfoCategory eventInfoCategory5 = createEventInfoCategory(event2, category1);
		em.persist(eventInfoCategory1);
		em.persist(eventInfoCategory2);
		em.persist(eventInfoCategory3);
		em.persist(eventInfoCategory4);
		em.persist(eventInfoCategory5);

		//when
		Long count = eventInfoCategoryRepository.countByEventCategory(category2);

		//then
		assertThat(count).isEqualTo(2);
	}

	Event createEvent(String storeName) {
		return Event.builder()
			.storeName(storeName)
			.build();
	}

	EventCategory createEventCategory(String category) {
		return EventCategory.builder()
			.category(category)
			.build();
	}

	EventInfoCategory createEventInfoCategory(Event event, EventCategory eventCategory) {
		return EventInfoCategory.builder()
			.event(event)
			.eventCategory(eventCategory)
			.build();
	}
}
