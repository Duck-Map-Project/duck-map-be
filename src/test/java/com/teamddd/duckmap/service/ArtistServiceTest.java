package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.artist.CreateArtistReq;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.ArtistType;
import com.teamddd.duckmap.repository.ArtistRepository;

@Transactional
@SpringBootTest
class ArtistServiceTest {

	@Autowired
	ArtistService artistService;
	@Autowired
	ArtistRepository artistRepository;
	@Autowired
	EntityManager em;

	@DisplayName("아티스트를 등록한다")
	@Test
	void createArtist() throws Exception {
		//given
		ArtistType artistType = createArtistType();
		em.persist(artistType);

		CreateArtistReq request = new CreateArtistReq();
		ReflectionTestUtils.setField(request, "name", "artist1");
		ReflectionTestUtils.setField(request, "image", "filename.png");
		ReflectionTestUtils.setField(request, "artistTypeId", artistType.getId());

		//when
		Long artistId = artistService.createArtist(request);

		//then
		assertThat(artistId).isNotNull();

		Optional<Artist> findArtist = artistRepository.findById(artistId);
		assertThat(findArtist).isNotEmpty();
		assertThat(findArtist.get()).extracting("id", "name")
			.contains(1L, "artist1");
	}

	ArtistType createArtistType() {
		return ArtistType.builder()
			.type("artistType")
			.build();
	}
}
