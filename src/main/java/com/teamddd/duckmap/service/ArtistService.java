package com.teamddd.duckmap.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.teamddd.duckmap.common.Props;
import com.teamddd.duckmap.dto.artist.ArtistRes;
import com.teamddd.duckmap.dto.artist.ArtistSearchParam;
import com.teamddd.duckmap.dto.artist.CreateArtistReq;
import com.teamddd.duckmap.dto.artist.UpdateArtistServiceReq;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.ArtistType;
import com.teamddd.duckmap.exception.NonExistentArtistException;
import com.teamddd.duckmap.repository.ArtistRepository;
import com.teamddd.duckmap.repository.EventArtistRepository;
import com.teamddd.duckmap.repository.LastSearchArtistRepository;
import com.teamddd.duckmap.util.FileUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistService {

	private final Props props;
	private final ArtistRepository artistRepository;
	private final ArtistTypeService artistTypeService;
	private final LastSearchArtistRepository lastSearchArtistRepository;
	private final EventArtistRepository eventArtistRepository;

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

	public List<Artist> getArtistsByIds(List<Long> ids) {
		List<Artist> artists = artistRepository.findByIdIn(ids);

		Set<Long> duplicatedIds = new HashSet<>(ids);
		if (duplicatedIds.size() == artists.size()) {
			return artists;
		}
		throw new NonExistentArtistException();
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

		return artistRepository.findByGroup(group).stream()
			.map(ArtistRes::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public void updateArtist(UpdateArtistServiceReq request) {
		Artist artist = getArtist(request.getId());

		Artist group = null;
		if (request.getGroupId() != null) {
			group = getArtist(request.getGroupId());
		}
		ArtistType artistType = artistTypeService.getArtistType(request.getArtistTypeId());

		String oldImage = artist.getImage();

		artist.updateArtist(group, request.getName(), request.getImage(), artistType);

		if (StringUtils.hasText(oldImage) && !oldImage.equals(request.getImage())) {
			FileUtils.deleteFile(props.getImageDir(), oldImage);
		}
	}

	@Transactional
	public void deleteArtist(Long id) {
		Artist artist = getArtist(id);

		// update group fk to null
		artistRepository.updateGroupToNull(artist.getId());
		// update artist fk to null
		eventArtistRepository.updateArtistToNull(artist.getId());
		// delete artist fk
		lastSearchArtistRepository.deleteByArtistId(artist.getId());
		// delete image
		if (StringUtils.hasText(artist.getImage())) {
			FileUtils.deleteFile(props.getImageDir(), artist.getImage());
		}

		artistRepository.delete(artist);
	}
}
