package com.teamddd.duckmap.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamddd.duckmap.dto.artist.ArtistTypeRes;
import com.teamddd.duckmap.dto.artist.CreateArtistTypeReq;
import com.teamddd.duckmap.service.ArtistTypeService;

@SpringBootTest
@AutoConfigureMockMvc
class ArtistTypeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ArtistTypeService artistTypeService;

	@DisplayName("아티스트 구분 전체 목록을 조회한다")
	@Test
	void getAllArtistType() throws Exception {
		//given
		assertThat(artistTypeService.getArtistTypes()).hasOnlyElementsOfType(ArtistTypeRes.class);

		//when //then
		mockMvc.perform(
				get("/artists/types")
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray());
	}

	@Nested
	@DisplayName("아티스트 타입을 생성한다")
	class CreateArtistType {
		@DisplayName("관리자 계정은 아티스트 타입을 생성할 수 있다")
		@Test
		@WithMockUser(roles = "ADMIN")
		void createArtistType1() throws Exception {
			//given
			CreateArtistTypeReq request = new CreateArtistTypeReq();
			ReflectionTestUtils.setField(request, "type", "type1");

			//when //then
			mockMvc.perform(
					post("/artists/types")
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk());
		}

		@DisplayName("사용자 계정은 아티스트 타입을 생성할 수 없다")
		@Test
		@WithMockUser(roles = "USER")
		void createArtistType2() throws Exception {
			//given
			CreateArtistTypeReq request = new CreateArtistTypeReq();
			ReflectionTestUtils.setField(request, "type", "type1");

			//when //then
			mockMvc.perform(
					post("/artists/types")
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
