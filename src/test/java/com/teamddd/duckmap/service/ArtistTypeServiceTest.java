package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.artist.ArtistTypeRes;
import com.teamddd.duckmap.dto.artist.CreateArtistTypeReq;
import com.teamddd.duckmap.entity.ArtistType;
import com.teamddd.duckmap.repository.ArtistTypeRepository;

@Transactional
@SpringBootTest
class ArtistTypeServiceTest {

	@Autowired
	ArtistTypeService artistTypeService;
	@Autowired
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
	void getArtistTypes() throws Exception {
		//given
		ArtistType type1 = createArtistType("그룹");
		ArtistType type2 = createArtistType("아이돌");
		ArtistType type3 = createArtistType("배우");
		artistTypeRepository.saveAll(List.of(type1, type2, type3));

		//when
		List<ArtistTypeRes> artistTypes = artistTypeService.getArtistTypes();

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

}
