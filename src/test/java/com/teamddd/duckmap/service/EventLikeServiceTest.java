package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventLike;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.AuthenticationRequiredException;
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
	@Autowired
	EntityManager em;

	@DisplayName("이벤트 좋아요")
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
		EventLike eventLike = eventLikeService.likeEvent(event.getId(), member);
		Long likeId = eventLike.getId();

		//then
		assertThat(likeId).isNotNull();

		Optional<EventLike> findLike = eventLikeRepository.findById(likeId);
		assertThat(findLike).isNotEmpty();
		assertThat(findLike.get())
			.extracting("event.storeName", "member.username")
			.containsOnly("store", "member1");
	}

	@DisplayName("이벤트 좋아요 취소")
	@Nested
	class DeleteLikeEvent {
		@DisplayName("로그인 사용자와 좋아요한 사용자가 같을때")
		@Test
		void deleteLikeEvent1() throws Exception {
			//given
			Member member = Member.builder()
				.username("member1")
				.build();
			em.persist(member);

			Event event = Event.builder()
				.storeName("store")
				.member(member)
				.build();
			em.persist(event);

			when(eventService.getEvent(any())).thenReturn(event);
			EventLike eventLike = eventLikeService.likeEvent(event.getId(), member);
			em.persist(eventLike);

			//when
			Long likeId = eventLike.getId();
			when(eventLikeRepository.findMemberById(likeId)).thenReturn(Optional.ofNullable(member));
			eventLikeService.deleteLikeEvent(likeId, member);

			//then
			Optional<EventLike> findLike = eventLikeRepository.findById(likeId);
			assertThat(findLike).isEmpty();
		}

		@DisplayName("로그인 사용자와 좋아요한 사용자가 다를때")
		@Test
		void deleteLikeEvent2() throws Exception {
			//given
			Member member = Member.builder()
				.username("member1")
				.build();
			Member loginMember = Member.builder()
				.username("member2")
				.build();
			em.persist(member);
			em.persist(loginMember);

			Event event = Event.builder()
				.storeName("store")
				.member(member)
				.build();
			em.persist(event);

			when(eventService.getEvent(event.getId())).thenReturn(event);
			EventLike eventLike = eventLikeService.likeEvent(event.getId(), member);
			em.persist(eventLike);

			Long likeId = eventLike.getId();
			when(eventLikeRepository.findMemberById(likeId)).thenReturn(Optional.ofNullable(member));

			//when //then
			assertThatThrownBy(() -> eventLikeService.deleteLikeEvent(likeId, loginMember))
				.isInstanceOf(AuthenticationRequiredException.class)
				.hasMessage("인증이 필요합니다");
		}
	}
}
