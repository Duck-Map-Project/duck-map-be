package com.teamddd.duckmap.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArtistTypeTest {

	@DisplayName("ArtistType의 값을 변경한다")
	@Test
	void updateArtistType() throws Exception {
		//given
		ArtistType artistType = createArtistType(1L, "type");

		//when
		artistType.updateArtistType("new_type");

		//then
		assertThat(artistType).extracting("id", "type")
			.containsExactly(1L, "new_type");
	}

	ArtistType createArtistType(Long id, String type) {
		return ArtistType.builder()
			.id(id)
			.type(type)
			.build();
	}
}
