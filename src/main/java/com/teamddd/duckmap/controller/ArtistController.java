package com.teamddd.duckmap.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.config.security.SecurityRule;
import com.teamddd.duckmap.dto.PageReq;
import com.teamddd.duckmap.dto.artist.ArtistRes;
import com.teamddd.duckmap.dto.artist.ArtistSearchParam;
import com.teamddd.duckmap.dto.artist.CreateArtistReq;
import com.teamddd.duckmap.dto.artist.CreateArtistRes;
import com.teamddd.duckmap.dto.artist.UpdateArtistReq;
import com.teamddd.duckmap.dto.artist.UpdateArtistServiceReq;
import com.teamddd.duckmap.service.ArtistService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/artists")
public class ArtistController {

	private final ArtistService artistService;

	@PreAuthorize(SecurityRule.HAS_ROLE_ADMIN)
	@Operation(summary = "아티스트 등록")
	@PostMapping
	public CreateArtistRes createArtist(
		@Validated @RequestBody CreateArtistReq createArtistReq) {

		Long artistId = artistService.createArtist(createArtistReq);

		return CreateArtistRes.builder()
			.id(artistId)
			.build();
	}

	@Operation(summary = "아티스트 목록 조회")
	@GetMapping
	public Page<ArtistRes> getArtists(ArtistSearchParam searchParam, PageReq pageReq) {
		Pageable pageable = PageRequest.of(pageReq.getPageNumber(), pageReq.getPageSize());
		return artistService.getArtistResPageByTypeAndName(searchParam, pageable);
	}

	@Operation(summary = "아티스트 pk로 소속 아티스트 목록 조회")
	@GetMapping("/{id}/artists")
	public List<ArtistRes> getArtistsByGroup(@PathVariable Long id) {
		return artistService.getArtistsByGroup(id);
	}

	@PreAuthorize(SecurityRule.HAS_ROLE_ADMIN)
	@Operation(summary = "아티스트 수정")
	@PutMapping("/{id}")
	public void updateArtist(@PathVariable Long id, @Validated @RequestBody UpdateArtistReq updateArtistReq) {
		UpdateArtistServiceReq request = UpdateArtistServiceReq.builder()
			.id(id)
			.groupId(updateArtistReq.getGroupId())
			.name(updateArtistReq.getName())
			.image(updateArtistReq.getImage())
			.artistTypeId(updateArtistReq.getArtistTypeId())
			.build();

		artistService.updateArtist(request);
	}

	@PreAuthorize(SecurityRule.HAS_ROLE_ADMIN)
	@Operation(summary = "아티스트 삭제")
	@DeleteMapping("/{id}")
	public void deleteArtist(@PathVariable Long id) {
		artistService.deleteArtist(id);
	}
}
