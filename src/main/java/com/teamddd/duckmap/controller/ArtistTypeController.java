package com.teamddd.duckmap.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.dto.ArtistTypeRes;
import com.teamddd.duckmap.dto.CreateArtistTypeReq;
import com.teamddd.duckmap.dto.CreateArtistTypeRes;
import com.teamddd.duckmap.dto.Result;
import com.teamddd.duckmap.dto.UpdateArtistTypeReq;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/artists/types")
public class ArtistTypeController {
	@Operation(summary = "아티스트 구분 등록")
	@PostMapping
	public Result<CreateArtistTypeRes> createArtistType(
		@Validated @RequestBody CreateArtistTypeReq createArtistTypeReq) {
		return Result.<CreateArtistTypeRes>builder()
			.data(
				CreateArtistTypeRes.builder()
					.id(1L)
					.build()
			)
			.build();
	}

	@Operation(summary = "아티스트 구분 모두 조회")
	@GetMapping
	public Page<ArtistTypeRes> getAllArtistType(Pageable pageable) {
		return new PageImpl<>(List.of(
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
				.build()
		));
	}

	@Operation(summary = "아티스트 구분 pk로 조회")
	@GetMapping("/{id}")
	public Result<ArtistTypeRes> getArtistType(@PathVariable Long id) {
		return Result.<ArtistTypeRes>builder()
			.data(
				ArtistTypeRes.builder()
					.id(id)
					.type("아이돌")
					.build()
			)
			.build();
	}

	@Operation(summary = "아티스트 구분 수정")
	@PutMapping("/{id}")
	public Result<Void> updateArtistType(@PathVariable Long id, @RequestBody UpdateArtistTypeReq updateArtistTypeReq) {
		return Result.<Void>builder().build();
	}

	@Operation(summary = "아티스트 구분 삭제")
	@DeleteMapping("/{id}")
	public Result<Void> deleteArtistType(@PathVariable Long id) {
		return Result.<Void>builder().build();
	}
}
