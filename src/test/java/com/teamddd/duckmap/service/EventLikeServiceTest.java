package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventLike;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.repository.EventLikeRepository;

@Transactional
@SpringBootTest
public class EventLikeServiceTest {
	@Autowired
	EventLikeService eventLikeService;
	@SpyBean
	EventLikeRepository eventLikeRepository;
	@MockBean
	EventService eventService;

	@DisplayName("이벤트 좋아요하기")
	@Test
	void likeEvent() throws Exception {
		//given
		Member member = Member.builder()
			.username("member1")
			.build();

		Event event = Event.builder()
			.storeName("store")
			.member(member)
			.build();

		when(eventService.getEvent(any())).thenReturn(event);
		//when
		Long likeId = eventLikeService.likeEvent(event.getId(), member);

		//then
		assertThat(likeId).isNotNull();

		Optional<EventLike> findLike = eventLikeRepository.findById(likeId);
		assertThat(findLike).isNotEmpty();
		assertThat(findLike.get())
			.extracting("event.storeName", "member.username")
			.containsOnly("store", "member1");
	}
}
