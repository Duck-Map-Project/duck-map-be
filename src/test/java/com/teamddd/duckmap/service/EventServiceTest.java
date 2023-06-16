package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.event.CreateEventReq;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventCategory;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.repository.EventRepository;

@Transactional
@SpringBootTest
class EventServiceTest {
	@Autowired
	EventService eventService;
	@SpyBean
	EventRepository eventRepository;
	@MockBean
	ArtistService artistService;
	@MockBean
	EventCategoryService eventCategoryService;

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

		Member member = Member.builder()
			.username("member1")
			.build();

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
		assertThat(findEvent.get())
			.extracting("storeName", "member.username")
			.containsOnly("store name", "member1");
	}

}
