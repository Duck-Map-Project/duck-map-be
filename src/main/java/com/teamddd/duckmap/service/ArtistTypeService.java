package com.teamddd.duckmap.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.artist.CreateArtistTypeReq;
import com.teamddd.duckmap.entity.ArtistType;
import com.teamddd.duckmap.repository.ArtistTypeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistTypeService {
	private final ArtistTypeRepository artistTypeRepository;

	@Transactional
	public Long createArtistType(CreateArtistTypeReq createArtistTypeReq) {
		ArtistType artistType = ArtistType.builder()
			.type(createArtistTypeReq.getType())
			.build();

		artistTypeRepository.save(artistType);

		return artistType.getId();
	}
}
