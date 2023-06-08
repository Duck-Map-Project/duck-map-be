package com.teamddd.duckmap.dto.artist;

import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.entity.Artist;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ArtistRes {
	private Long id;
	private Long groupId;
	private String groupName;
	private String name;
	private ImageRes image;
	private ArtistTypeRes artistType;

	public static ArtistRes of(Artist artist) {
		ArtistResBuilder artistResBuilder = ArtistRes.builder()
			.id(artist.getId())
			.name(artist.getName())
			.image(
				ImageRes.builder()
					.filename(artist.getImage())
					.build()
			)
			.artistType(
				ArtistTypeRes.of(artist.getArtistType())
			);

		if (artist.getGroup() != null) {
			artistResBuilder
				.groupId(artist.getGroup().getId())
				.groupName(artist.getGroup().getName());
		}

		return artistResBuilder.build();
	}
}
