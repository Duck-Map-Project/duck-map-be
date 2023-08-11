package com.teamddd.duckmap.dto.artist;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.ArtistType;

@Transactional
@SpringBootTest
class ArtistTypeResTest {

	@Autowired
	EntityManager em;

	@DisplayName("ArtistType을 ArtistTypeRes로 변환")
	@Test
	void of() throws Exception {
		//given
		ArtistType type1 = ArtistType.builder()
			.type("type1")
			.build();
		em.persist(type1);

		em.flush();
		em.clear();

		//when
		ArtistTypeRes artistTypeRes = ArtistTypeRes.of(type1);

		//then
		assertThat(artistTypeRes)
			.extracting("id", "type")
			.containsExactly(type1.getId(), "type1");
	}

}
