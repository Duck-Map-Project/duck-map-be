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

import com.teamddd.duckmap.dto.ArtistRes;
import com.teamddd.duckmap.dto.ArtistSearchParam;
import com.teamddd.duckmap.dto.ArtistTypeRes;
import com.teamddd.duckmap.dto.CreateArtistReq;
import com.teamddd.duckmap.dto.CreateArtistRes;
import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.dto.Result;
import com.teamddd.duckmap.dto.UpdateArtistReq;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/artists")
public class ArtistController {
	@Operation(summary = "아티스트 등록")
	@PostMapping
	public Result<CreateArtistRes> createArtist(
		@Validated @RequestBody CreateArtistReq createArtistReq) {
		return Result.<CreateArtistRes>builder()
			.data(
				CreateArtistRes.builder()
					.id(1L)
					.build()
			)
			.build();
	}

	@Operation(summary = "아티스트 목록 조회")
	@GetMapping
	public Page<ArtistRes> getAllArtists(Pageable pageable, ArtistSearchParam searchParam) {
		return new PageImpl<>(List.of(
			ArtistRes.builder()
				.id(1L)
				.groupId(null)
				.name("세븐틴")
				.image(
					ImageRes.builder()
						.apiUrl("/images/")
						.filename("artist_img_svt.jpg")
						.build()
				)
				.artistType(
					ArtistTypeRes.builder()
						.id(1L)
						.type("아이돌")
						.build()
				)
				.build(),
			ArtistRes.builder()
				.id(2L)
				.groupId(null)
				.name("이제훈")
				.image(
					ImageRes.builder()
						.apiUrl("/images/")
						.filename("artist_img_ljh.jpg")
						.build()
				)
				.artistType(
					ArtistTypeRes.builder()
						.id(2L)
						.type("배우")
						.build()
				)
				.build(),
			ArtistRes.builder()
				.id(3L)
				.groupId(null)
				.name("아이브")
				.image(
					ImageRes.builder()
						.apiUrl("/images/")
						.filename("artist_img_ive.jpg")
						.build()
				)
				.artistType(
					ArtistTypeRes.builder()
						.id(1L)
						.type("아이돌")
						.build()
				)
				.build()
		));
	}

	@Operation(summary = "아티스트 pk로 조회")
	@GetMapping("/{id}")
	public Result<ArtistRes> getArtist(@PathVariable Long id) {
		return Result.<ArtistRes>builder()
			.data(
				ArtistRes.builder()
					.id(id)
					.groupId(null)
					.name("세븐틴")
					.image(
						ImageRes.builder()
							.apiUrl("/images/")
							.filename("artist_img_svt.jpg")
							.build()
					)
					.artistType(
						ArtistTypeRes.builder()
							.id(1L)
							.type("아이돌")
							.build()
					)
					.build()
			)
			.build();
	}

	@Operation(summary = "아티스트 수정")
	@PutMapping("/{id}")
	public Result<Void> updateArtist(@PathVariable Long id, @Validated @RequestBody UpdateArtistReq updateArtistReq) {
		return Result.<Void>builder().build();
	}

	@Operation(summary = "아티스트 삭제")
	@DeleteMapping("/{id}")
	public Result<Void> deleteArtist(@PathVariable Long id) {
		return Result.<Void>builder().build();
	}
}