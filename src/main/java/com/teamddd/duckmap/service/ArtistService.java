package com.teamddd.duckmap.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.artist.ArtistRes;
import com.teamddd.duckmap.dto.artist.ArtistSearchParam;
import com.teamddd.duckmap.dto.artist.CreateArtistReq;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.ArtistType;
import com.teamddd.duckmap.exception.NonExistentArtistException;
import com.teamddd.duckmap.repository.ArtistRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistService {

	private final ArtistRepository artistRepository;
	private final ArtistTypeService artistTypeService;

	@Transactional
	public Long createArtist(CreateArtistReq createArtistReq) {
		ArtistType artistType = artistTypeService.getArtistType(createArtistReq.getArtistTypeId());
		Artist group = null;
		if (createArtistReq.getGroupId() != null) {
			group = getArtist(createArtistReq.getGroupId());
		}

		Artist artist = Artist.builder()
			.artistType(artistType)
			.name(createArtistReq.getName())
			.image(createArtistReq.getImage())
			.group(group)
			.build();

		artistRepository.save(artist);

		return artist.getId();
	}

	public Artist getArtist(Long artistId) throws NonExistentArtistException {
		return artistRepository.findById(artistId)
			.orElseThrow(NonExistentArtistException::new);
	}

	public Page<ArtistRes> getArtistResPageByTypeAndName(ArtistSearchParam artistSearchParam, Pageable pageable) {
		Page<Artist> artistPage = artistRepository.findByTypeAndName(
			artistSearchParam.getArtistTypeId(),
			artistSearchParam.getArtistName(),
			pageable);

		List<ArtistRes> artistResList = artistPage.getContent().stream()
			.map(ArtistRes::of)
			.collect(Collectors.toList());

		return new PageImpl<>(artistResList, artistPage.getPageable(), artistPage.getTotalElements());
	}

	public List<ArtistRes> getArtistsByGroup(Long groupId) {
		Artist group = getArtist(groupId);

		List<Artist> artists = artistRepository.findByGroup(group);

		return artists.stream()
			.map(ArtistRes::of)
			.collect(Collectors.toList());
	}
}
