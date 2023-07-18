package com.teamddd.duckmap.dto.user;

import com.teamddd.duckmap.entity.LastSearchArtist;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LastSearchArtistRes {
	private Long memberId;
	private Long artistId;

	public static LastSearchArtistRes of(LastSearchArtist lastSearchArtist) {
		return LastSearchArtistRes.builder()
			.memberId(lastSearchArtist.getMember().getId())
			.artistId(lastSearchArtist.getArtist().getId())
			.build();
	}
}
