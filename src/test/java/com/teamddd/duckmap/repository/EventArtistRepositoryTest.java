package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventArtist;

@Transactional
@SpringBootTest
class EventArtistRepositoryTest {
	@Autowired
	EntityManager em;

	@Autowired
	EventArtistRepository eventArtistRepository;

	@DisplayName("Event FK로 EventArtist 목록을 제거한다")
	@Test
	void deleteByEventId() throws Exception {
		//given
		Event event1 = createEvent("event1");
		em.persist(event1);

		EventArtist eventArtist1 = createEventArtist(event1, null);
		EventArtist eventArtist2 = createEventArtist(event1, null);
		em.persist(eventArtist1);
		em.persist(eventArtist2);

		em.flush();
		em.clear();

		//when
		int count = eventArtistRepository.deleteByEventId(event1.getId());

		//then
		assertThat(count).isEqualTo(2);

		List<EventArtist> findEventArtists = eventArtistRepository.findAllById(
			List.of(eventArtist1.getId(), eventArtist2.getId()));
		assertThat(findEventArtists).isEmpty();
	}

	Event createEvent(String storeName) {
		return Event.builder()
			.storeName(storeName)
			.build();
	}

	EventArtist createEventArtist(Event event, Artist artist) {
		return EventArtist.builder()
			.event(event)
			.artist(artist)
			.build();
	}

}
