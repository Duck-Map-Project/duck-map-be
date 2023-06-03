package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

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

}
