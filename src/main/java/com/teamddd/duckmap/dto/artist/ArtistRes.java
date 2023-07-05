package com.teamddd.duckmap.dto.artist;

import com.teamddd.duckmap.common.ApiUrl;
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
	private String image;
	private ArtistTypeRes artistType;

	public static ArtistRes of(Artist artist) {
		ArtistResBuilder artistResBuilder = ArtistRes.builder()
			.id(artist.getId())
			.name(artist.getName())
			.image(ApiUrl.IMAGE + artist.getImage())
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
