package com.teamddd.duckmap.dto.artist;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.ArtistType;

class ArtistResTest {

	private Artist createArtist(long id, ArtistType type, String name, String image, Artist group) {
		return Artist.builder()
			.id(id)
			.artistType(type)
			.name(name)
			.image(image)
			.group(group)
			.build();
	}

	private ArtistType createType(long id, String type) {
		return ArtistType.builder()
			.id(id)
			.type(type)
			.build();
	}

	@DisplayName("Artist를 ArtistRes로 변환한다")
	@Nested
	class Of {
		@DisplayName("artist.getGroup() == null")
		@Test
		void of1() throws Exception {
			//given
			ArtistType type1 = createType(1L, "type1");
			ArtistType type2 = createType(2L, "type2");

			Artist artist1 = createArtist(1L, type1, "artist1", "image1", null);

			//when
			ArtistRes artistRes = ArtistRes.of(artist1);

			//then
			assertThat(artistRes).extracting("artistType.type", "name", "groupName", "image")
				.contains("type1", "artist1", null, "/images/image1");
		}

		@DisplayName("artist.getGroup() != null")
		@Test
		void of2() throws Exception {
			//given
			ArtistType type1 = createType(1L, "type1");
			ArtistType type2 = createType(2L, "type2");

			Artist artist1 = createArtist(1L, type1, "artist1", "image1", null);
			Artist artist2 = createArtist(1L, type2, "artist2", "image2", artist1);

			//when
			ArtistRes artistRes = ArtistRes.of(artist2);

			//then
			assertThat(artistRes).extracting("artistType.type", "name", "groupName", "image")
				.contains("type2", "artist2", "artist1", "/images/image2");
		}
	}

}
