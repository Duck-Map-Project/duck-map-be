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

	@DisplayName("EventArtist artist를 null로 변경한다")
	@Test
	void updateArtistToNull() throws Exception {
		//given
		Artist artist1 = createArtist("artist1");
		Artist artist2 = createArtist("artist2");
		em.persist(artist1);
		em.persist(artist2);

		EventArtist eventArtist1 = createEventArtist(null, artist1);
		EventArtist eventArtist2 = createEventArtist(null, artist2);
		EventArtist eventArtist3 = createEventArtist(null, artist1);
		EventArtist eventArtist4 = createEventArtist(null, artist2);
		em.persist(eventArtist1);
		em.persist(eventArtist2);
		em.persist(eventArtist3);
		em.persist(eventArtist4);

		//when
		int count = eventArtistRepository.updateArtistToNull(artist1.getId());

		//then
		assertThat(count).isEqualTo(2);

		List<EventArtist> findEventArtists = eventArtistRepository.findAll();
		assertThat(findEventArtists).hasSize(4)
			.extracting("artist.name")
			.containsExactly(null, "artist2", null, "artist2");
	}

	Event createEvent(String storeName) {
		return Event.builder()
			.storeName(storeName)
			.build();
	}

	Artist createArtist(String name) {
		return Artist.builder()
			.name(name)
			.build();
	}

	EventArtist createEventArtist(Event event, Artist artist) {
		return EventArtist.builder()
			.event(event)
			.artist(artist)
			.build();
	}

}
