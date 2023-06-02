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

import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.dto.Result;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderMemberRes;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderRes;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkedEventRes;
import com.teamddd.duckmap.dto.event.bookmark.CreateBookmarkFolderReq;
import com.teamddd.duckmap.dto.event.bookmark.CreateBookmarkFolderRes;
import com.teamddd.duckmap.dto.event.bookmark.UpdateBookmarkFolderReq;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark-folders")
public class BookmarkFolderController {
	@Operation(summary = "북마크 폴더 생성")
	@PostMapping
	public Result<CreateBookmarkFolderRes> createBookmarkFolder(
		@Validated @RequestBody CreateBookmarkFolderReq createBookmarkFolderReq) {
		return Result.<CreateBookmarkFolderRes>builder()
			.data(
				CreateBookmarkFolderRes.builder()
					.id(1L)
					.build()
			)
			.build();
	}

	@Operation(summary = "로그인한 회원이 생성한 북마크 폴더 목록 조회")
	@GetMapping
	public Page<BookmarkFolderRes> getAllBookmarkFolders(Pageable pageable) {
		return new PageImpl<>(List.of(
			BookmarkFolderRes.builder()
				.id(1L)
				.name("Folder1")
				.image(
					ImageRes.builder()
						.apiUrl("/images/")
						.filename("default_folder.jpg")
						.build()
				)
				.build(),
			BookmarkFolderRes.builder()
				.id(2L)
				.name("Folder2")
				.image(
					ImageRes.builder()
						.apiUrl("/images/")
						.filename("default_folder.jpg")
						.build()
				)
				.build()
		));
	}

	@Operation(summary = "북마크 폴더 pk로 북마크 폴더,사용자 정보 조회", description = "북마크 폴더 외부 공유용 api")
	@GetMapping("/{id}")
	public Result<BookmarkFolderMemberRes> getBookmarkFolder(@PathVariable Long id) {
		return Result.<BookmarkFolderMemberRes>builder()
			.data(
				BookmarkFolderMemberRes.builder()
					.id(1L)
					.name("생일카페 모음")
					.image(
						ImageRes.builder()
							.apiUrl("/images/")
							.filename("default_folder.jpg")
							.build()
					)
					.memberId(1L)
					.username("사용자1")
					.build()
			)
			.build();
	}

	@Operation(summary = "북마크 폴더 내부의 이벤트 목록 조회")
	@GetMapping("/{id}/events")
	public Page<BookmarkedEventRes> getAllBookmarks(@PathVariable Long id, Pageable pageable) {
		return new PageImpl<>(List.of(
			BookmarkedEventRes.builder()
				.id(1L)
				.storeName("스프링 카페")
				.image(
					ImageRes.builder()
						.apiUrl("/images/")
						.filename("default_cafe.jpg")
						.build()
				).build(),
			BookmarkedEventRes.builder()
				.id(2L)
				.storeName("썸머 카페")
				.image(
					ImageRes.builder()
						.apiUrl("/images/")
						.filename("default_cafe.jpg")
						.build()
				).build()
		));
	}

	@Operation(summary = "북마크 폴더명, 이미지 변경 요청")
	@PutMapping("/{id}")
	public Result<Void> updateBookmarkFolder(@PathVariable Long id,
		@Validated @RequestBody UpdateBookmarkFolderReq updateBookmarkFolderReq) {
		return Result.<Void>builder().build();
	}

	@Operation(summary = "북마크 폴더 삭제")
	@DeleteMapping("/{id}")
	public Result<Void> deleteBookmarkFolder(@PathVariable Long id) {
		return Result.<Void>builder().build();
	}
}
