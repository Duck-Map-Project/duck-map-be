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
import com.teamddd.duckmap.entity.EventImage;

@Transactional
@SpringBootTest
class EventImageRepositoryTest {
	@Autowired
	EntityManager em;

	@Autowired
	EventImageRepository eventImageRepository;

	@DisplayName("Event FK로 EventImage 목록을 제거한다")
	@Test
	void deleteByEventId() throws Exception {
		//given
		Event event1 = createEvent("event1");
		em.persist(event1);

		EventImage eventImage1 = createEventImage(event1, null);
		EventImage eventImage2 = createEventImage(event1, null);
		em.persist(eventImage1);
		em.persist(eventImage2);

		em.flush();
		em.clear();

		//when
		int count = eventImageRepository.deleteByEventId(event1.getId());

		//then
		assertThat(count).isEqualTo(2);

		List<EventImage> findEventImages = eventImageRepository.findAllById(
			List.of(eventImage1.getId(), eventImage2.getId()));
		assertThat(findEventImages).isEmpty();
	}

	Event createEvent(String storeName) {
		return Event.builder()
			.storeName(storeName)
			.build();
	}

	EventImage createEventImage(Event event, String image) {
		return EventImage.builder()
			.event(event)
			.image(image)
			.build();
	}

}
