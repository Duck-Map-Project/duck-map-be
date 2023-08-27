package com.teamddd.duckmap.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.artist.ArtistTypeRes;
import com.teamddd.duckmap.dto.artist.CreateArtistTypeReq;
import com.teamddd.duckmap.dto.artist.UpdateArtistTypeServiceReq;
import com.teamddd.duckmap.entity.ArtistType;
import com.teamddd.duckmap.exception.NonExistentArtistTypeException;
import com.teamddd.duckmap.exception.UnableToDeleteArtistTypeInUseException;
import com.teamddd.duckmap.repository.ArtistRepository;
import com.teamddd.duckmap.repository.ArtistTypeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistTypeService {
	private final ArtistTypeRepository artistTypeRepository;
	private final ArtistRepository artistRepository;

	@Transactional
	public Long createArtistType(CreateArtistTypeReq createArtistTypeReq) {
		ArtistType artistType = ArtistType.builder()
			.type(createArtistTypeReq.getType())
			.build();

		artistTypeRepository.save(artistType);

		return artistType.getId();
	}

	public ArtistType getArtistType(Long artistTypeId) throws NonExistentArtistTypeException {
		return artistTypeRepository.findById(artistTypeId)
			.orElseThrow(NonExistentArtistTypeException::new);
	}

	public List<ArtistTypeRes> getArtistTypeResList() {
		List<ArtistType> types = artistTypeRepository.findAll();
		return types.stream()
			.map(ArtistTypeRes::of)
			.toList();
	}

	@Transactional
	public void updateArtistType(UpdateArtistTypeServiceReq request) {
		ArtistType artistType = getArtistType(request.getId());

		artistType.updateArtistType(request.getType());
	}

	@Transactional
	public void deleteArtistType(Long id) {
		ArtistType type = getArtistType(id);

		Long artistCount = artistRepository.countByArtistType(type);
		if (artistCount > 0) {
			throw new UnableToDeleteArtistTypeInUseException();
		}

		artistTypeRepository.delete(type);
	}
}
