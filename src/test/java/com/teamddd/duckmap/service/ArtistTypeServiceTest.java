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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.artist.ArtistTypeRes;
import com.teamddd.duckmap.dto.artist.CreateArtistTypeReq;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.ArtistType;
import com.teamddd.duckmap.exception.NonExistentArtistTypeException;
import com.teamddd.duckmap.exception.UnableToDeleteArtistTypeInUseException;
import com.teamddd.duckmap.repository.ArtistRepository;
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
	@MockBean
	ArtistRepository artistRepository;

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

	ArtistType createArtistType(String type) {
		return ArtistType.builder()
			.type(type)
			.build();
	}

	Artist createArtist(ArtistType type, String name) {
		return Artist.builder()
			.artistType(type)
			.name(name)
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

	@DisplayName("아티스트 구분을 삭제한다")
	@Nested
	class DeleteArtistType {
		@DisplayName("사용중인 아티스트가 없는 경우 정상적으로 삭제된다")
		@Test
		void deleteArtistType1() throws Exception {
			//given
			ArtistType type = createArtistType("type");
			em.persist(type);

			em.flush();
			em.clear();

			Long deleteArtistTypeId = type.getId();

			when(artistRepository.countByArtistType(any())).thenReturn(0L);

			//when
			artistTypeService.deleteArtistType(deleteArtistTypeId);

			//then
			Optional<ArtistType> findArtistType = artistTypeRepository.findById(deleteArtistTypeId);
			assertThat(findArtistType).isEmpty();
		}

		@DisplayName("사용중인 아티스트가 있는 경우 삭제하지 않고 예외 발생")
		@Test
		void deleteArtistType2() throws Exception {
			//given
			ArtistType type = createArtistType("type");
			em.persist(type);

			Artist artist1 = createArtist(type, "artist1");
			Artist artist2 = createArtist(type, "artist2");
			em.persist(artist1);
			em.persist(artist2);

			em.flush();
			em.clear();

			Long deleteArtistTypeId = type.getId();

			when(artistRepository.countByArtistType(any())).thenReturn(2L);

			//when //then
			assertThatThrownBy(() -> artistTypeService.deleteArtistType(deleteArtistTypeId))
				.isInstanceOf(UnableToDeleteArtistTypeInUseException.class)
				.hasMessage("해당 아티스트 구분을 사용중인 아티스트가 존재하여 삭제할 수 없습니다");
		}
	}
}
