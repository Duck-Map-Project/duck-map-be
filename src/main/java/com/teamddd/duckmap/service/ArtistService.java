package com.teamddd.duckmap.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.artist.CreateArtistReq;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.ArtistType;
import com.teamddd.duckmap.repository.ArtistRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistService {

	private final ArtistRepository artistRepository;

	@Transactional
	public Long createArtist(CreateArtistReq createArtistReq) {
		Artist artist = Artist.builder()
			.artistType(
				ArtistType.builder()
					.id(createArtistReq.getArtistTypeId())
					.build()
			)
			.name(createArtistReq.getName())
			.image(createArtistReq.getImage())
			.group(
				Artist.builder()
					.id(createArtistReq.getGroupId())
					.build()
			).build();

		artistRepository.save(artist);

		return artist.getId();
	}
}
