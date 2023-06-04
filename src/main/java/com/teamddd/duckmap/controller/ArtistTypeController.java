package com.teamddd.duckmap.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.dto.artist.ArtistTypeRes;
import com.teamddd.duckmap.dto.artist.CreateArtistTypeReq;
import com.teamddd.duckmap.dto.artist.CreateArtistTypeRes;
import com.teamddd.duckmap.dto.artist.UpdateArtistTypeReq;
import com.teamddd.duckmap.service.ArtistTypeService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/artists/types")
public class ArtistTypeController {

	private final ArtistTypeService artistTypeService;

	@Operation(summary = "아티스트 구분 등록")
	@PostMapping
	public CreateArtistTypeRes createArtistType(
		@Validated @RequestBody CreateArtistTypeReq createArtistTypeReq) {

		Long artistTypeId = artistTypeService.createArtistType(createArtistTypeReq);

		return CreateArtistTypeRes.builder()
			.id(artistTypeId)
			.build();
	}

	@Operation(summary = "아티스트 구분 모두 조회")
	@GetMapping
	public List<ArtistTypeRes> getAllArtistType() {
		return List.of(
			ArtistTypeRes.builder()
				.id(1L)
				.type("아이돌")
				.build(),
			ArtistTypeRes.builder()
				.id(2L)
				.type("배우")
				.build(),
			ArtistTypeRes.builder()
				.id(3L)
				.type("모델")
				.build());
	}

	@Operation(summary = "아티스트 구분 pk로 조회")
	@GetMapping("/{id}")
	public ArtistTypeRes getArtistType(@PathVariable Long id) {
		return ArtistTypeRes.builder()
			.id(id)
			.type("아이돌")
			.build();
	}

	@Operation(summary = "아티스트 구분 수정")
	@PutMapping("/{id}")
	public void updateArtistType(@PathVariable Long id,
		@Validated @RequestBody UpdateArtistTypeReq updateArtistTypeReq) {
	}

	@Operation(summary = "아티스트 구분 삭제")
	@DeleteMapping("/{id}")
	public void deleteArtistType(@PathVariable Long id) {
	}
}
