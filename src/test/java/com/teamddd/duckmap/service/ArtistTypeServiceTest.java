package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.artist.ArtistTypeRes;
import com.teamddd.duckmap.dto.artist.CreateArtistTypeReq;
import com.teamddd.duckmap.dto.artist.UpdateArtistTypeServiceReq;
import com.teamddd.duckmap.entity.ArtistType;
import com.teamddd.duckmap.exception.NonExistentArtistTypeException;
import com.teamddd.duckmap.repository.ArtistTypeRepository;

@Transactional
@SpringBootTest
class ArtistTypeServiceTest {

	@Autowired
	EntityManager em;
	@Autowired
	ArtistTypeService artistTypeService;
	@SpyBean
	ArtistTypeRepository artistTypeRepository;

	@DisplayName("아티스트 타입을 받아 생성한다")
	@Test
	void createArtistType() throws Exception {
		//given
		CreateArtistTypeReq request = new CreateArtistTypeReq();
		ReflectionTestUtils.setField(request, "type", "type1");

		//when
		Long artistTypeId = artistTypeService.createArtistType(request);

		//then
		assertThat(artistTypeId).isNotNull();

		Optional<ArtistType> findArtistType = artistTypeRepository.findById(artistTypeId);
		assertThat(findArtistType).isNotEmpty();
		assertThat(findArtistType.get())
			.extracting("type")
			.isEqualTo("type1");
	}

	@DisplayName("아티스트 타입 목록 전체를 조회한다")
	@Test
	void getArtistTypeResList() throws Exception {
		//given
		ArtistType type1 = createArtistType("그룹");
		ArtistType type2 = createArtistType("아이돌");
		ArtistType type3 = createArtistType("배우");
		artistTypeRepository.saveAll(List.of(type1, type2, type3));

		//when
		List<ArtistTypeRes> artistTypes = artistTypeService.getArtistTypeResList();

		//then
		assertThat(artistTypes).hasSize(3)
			.extracting("type")
			.containsExactlyInAnyOrder("그룹", "아이돌", "배우");
	}

	@DisplayName("아티스트 타입을 변경한다")
	@Test
	void updateArtistType() throws Exception {
		//given
		ArtistType type = createArtistType("type");
		em.persist(type);

		em.flush();
		em.clear();

		Long updateTypeId = type.getId();
		UpdateArtistTypeServiceReq request = UpdateArtistTypeServiceReq.builder()
			.id(updateTypeId)
			.type("new_type")
			.build();

		when(artistTypeRepository.findById(updateTypeId)).thenReturn(Optional.of(type));

		//when
		artistTypeService.updateArtistType(request);
		em.flush();
		em.clear();

		//then
		ArtistType findArtistType = artistTypeRepository.findById(updateTypeId).get();
		assertThat(findArtistType).extracting("type").isEqualTo("new_type");
	}

	ArtistType createArtistType(String type) {
		return ArtistType.builder()
			.type(type)
			.build();
	}

	@DisplayName("아티스트 타입을 조회한다")
	@Nested
	class GetArtistType {
		@DisplayName("유효한 값으로 아티스트 타입을 조회한다")
		@Test
		void getArtistType1() throws Exception {
			//given
			ArtistType type = createArtistType("type1");
			artistTypeRepository.save(type);

			Long artistTypeId = type.getId();

			//when
			ArtistType findArtistType = artistTypeService.getArtistType(artistTypeId);

			//then
			assertThat(findArtistType).extracting("id", "type")
				.contains(artistTypeId, "type1");
		}

		@DisplayName("잘못된 값으로 아티스트 타입을 조회할 수 없다")
		@Test
		void getArtistType2() throws Exception {
			//given
			Long artistTypeId = 1L;

			//when //then
			assertThatThrownBy(() -> artistTypeService.getArtistType(artistTypeId))
				.isInstanceOf(NonExistentArtistTypeException.class)
				.hasMessage("잘못된 아티스트 구분 정보입니다");
		}
	}

}
