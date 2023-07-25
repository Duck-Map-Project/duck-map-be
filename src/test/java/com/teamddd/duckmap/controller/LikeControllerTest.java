package com.teamddd.duckmap.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.teamddd.duckmap.config.test.WithMockAuthUser;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventLike;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.service.EventLikeService;
import com.teamddd.duckmap.service.EventService;
import com.teamddd.duckmap.util.MemberUtils;

@SpringBootTest
@AutoConfigureMockMvc
public class LikeControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private EventLikeService likeService;
	@MockBean
	private EventService eventService;

	@DisplayName("이벤트 좋아요")
	@Test
	@WithMockAuthUser(email = "test2@email.com")
	void likeEvent() throws Exception {

		Member member = MemberUtils.getAuthMember().getUser();
		Member mockMember = Member.builder()
			.username("member1")
			.build();

		Event event = Event.builder()
			.storeName("store")
			.member(mockMember)
			.build();

		EventLike eventLike = EventLike.builder()
			.event(event)
			.member(member)
			.build();

		Long eventId = 1L;
		when(eventService.getEvent(eventId)).thenReturn(event);
		when(likeService.likeEvent(eventId, member)).thenReturn(eventLike);

		mockMvc.perform(post("/events/{id}/likes", eventId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(eventLike.getId()));

	}

	@DisplayName("이벤트 좋아요 취소")
	@Test
	@WithMockAuthUser
	void deleteLikeEvent() throws Exception {

		Member member = MemberUtils.getAuthMember().getUser();
		Member mockMember = Member.builder()
			.username("member1")
			.build();

		Event event = Event.builder()
			.storeName("store")
			.member(mockMember)
			.build();

		EventLike eventLike = EventLike.builder()
			.event(event)
			.member(member)
			.build();

		Long eventId = 1L;
		when(eventService.getEvent(eventId)).thenReturn(event);
		when(likeService.likeEvent(eventId, member)).thenReturn(eventLike);

		mockMvc.perform(delete("/events/{id}/likes", eventId))
			.andExpect(status().isOk());

	}

}
