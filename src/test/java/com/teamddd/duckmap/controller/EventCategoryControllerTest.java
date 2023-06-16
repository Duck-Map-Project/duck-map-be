package com.teamddd.duckmap.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamddd.duckmap.dto.event.category.CreateEventCategoryReq;
import com.teamddd.duckmap.dto.event.category.EventCategoryRes;
import com.teamddd.duckmap.service.EventCategoryService;

@SpringBootTest
@AutoConfigureMockMvc
class EventCategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private EventCategoryService eventCategoryService;

	@DisplayName("이벤트 카테고리 전체 목록을 조회한다")
	@Test
	void getEventCategories() throws Exception {
		//given
		List<EventCategoryRes> result = List.of();
		when(eventCategoryService.getEventCategoryResList()).thenReturn(result);

		//when //then
		mockMvc.perform(
				get("/events/categories")
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray());
	}

	@DisplayName("이벤트 카테고리를 생성한다")
	@Nested
	class CreateEventCategory {

		@DisplayName("관리자 계정은 이벤트 카테고리를 생성할 수 있다")
		@Test
		@WithMockUser(roles = "ADMIN")
		void createEventCategory1() throws Exception {
			//given
			CreateEventCategoryReq request = new CreateEventCategoryReq();
			ReflectionTestUtils.setField(request, "category", "category1");

			when(eventCategoryService.createEventCategory(any())).thenReturn(1L);

			//when //then
			mockMvc.perform(
					post("/events/categories")
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value("1"));
		}

		@DisplayName("사용자 계정은 이벤트 카테고리를 생성할 수 없다")
		@Test
		@WithMockUser(roles = "USER")
		void createEventCategory2() throws Exception {
			//given
			CreateEventCategoryReq request = new CreateEventCategoryReq();
			ReflectionTestUtils.setField(request, "category", "category1");

			//when //then
			mockMvc.perform(
					post("/events/categories")
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.code").value("A004"))
				.andExpect(jsonPath("$.message").value("권한이 없는 사용자입니다"));
		}
	}

}
