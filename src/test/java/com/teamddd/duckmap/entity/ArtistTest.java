package com.teamddd.duckmap.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArtistTest {

	@DisplayName("Artist의 값을 변경한다")
	@Test
	void updateArtist() throws Exception {
		//given
		Artist artist = createArtist(1L, null, "artist1", "image", null);

		Artist group = createArtist(2L, null, "group", "image", null);
		ArtistType artistType = createArtistType(1L, "artistType");

		//when
		artist.updateArtist(group, "new_name", "new_image", artistType);

		//then
		assertThat(artist).extracting("id", "group.name", "name", "image", "artistType.type")
			.containsExactly(1L, "group", "new_name", "new_image", "artistType");
	}

	Artist createArtist(Long id, Artist group, String name, String image, ArtistType artistType) {
		return Artist.builder()
			.id(id)
			.group(group)
			.name(name)
			.image(image)
			.artistType(artistType)
			.build();

	}

	ArtistType createArtistType(Long id, String type) {
		return ArtistType.builder()
			.id(id)
			.type(type)
			.build();
	}
}
