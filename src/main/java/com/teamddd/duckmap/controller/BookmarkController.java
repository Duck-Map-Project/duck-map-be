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

import com.teamddd.duckmap.dto.BookmarkFolderRes;
import com.teamddd.duckmap.dto.BookmarkRes;
import com.teamddd.duckmap.dto.BookmarkedEventRes;
import com.teamddd.duckmap.dto.CreateBookmarkFolderReq;
import com.teamddd.duckmap.dto.CreateBookmarkFolderRes;
import com.teamddd.duckmap.dto.CreateBookmarkReq;
import com.teamddd.duckmap.dto.CreateBookmarkRes;
import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.dto.Result;
import com.teamddd.duckmap.dto.UpdateBookmarkFolderReq;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {

	@Operation(summary = "북마크 생성")
	@PostMapping
	public Result<CreateBookmarkRes> createBookmark(
		@Validated @RequestBody CreateBookmarkReq createBookmarkReq) {
		return Result.<CreateBookmarkRes>builder()
			.data(
				CreateBookmarkRes.builder()
					.id(1L)
					.build()
			)
			.build();
	}

	@Operation(summary = "북마크 목록 조회")
	@GetMapping
	public Page<BookmarkRes> getAllBookmarks(Pageable pageable) {
		return new PageImpl<>(List.of(
			BookmarkRes.builder()
				.id(1L)
				.bookmarkedEventRes(
					BookmarkedEventRes.builder()
						.id(1L)
						.storeName("스프링 카페")
						.image(
							ImageRes.builder()
								.apiUrl("/images/")
								.filename("event_img.jpg")
								.build()
						)
						.build()
				)
				.build(),
			BookmarkRes.builder()
				.id(2L)
				.bookmarkedEventRes(
					BookmarkedEventRes.builder()
						.id(2L)
						.storeName("카페 언노운")
						.image(
							ImageRes.builder()
								.apiUrl("/images/")
								.filename("event_img.jpg")
								.build()
						)
						.build()
				)
				.build(),
			BookmarkRes.builder()
				.id(3L)
				.bookmarkedEventRes(
					BookmarkedEventRes.builder()
						.id(3L)
						.storeName("곱창 파는 고깃집")
						.image(
							ImageRes.builder()
								.apiUrl("/images/")
								.filename("event_img.jpg")
								.build()
						)
						.build()
				)
				.build()
		));
	}

	@Operation(summary = "북마크 pk로 조회")
	@GetMapping("/{id}")
	public Result<BookmarkRes> getBookmark(@PathVariable Long id) {
		return Result.<BookmarkRes>builder()
			.data(
				BookmarkRes.builder()
					.id(1L)
					.bookmarkedEventRes(
						BookmarkedEventRes.builder()
							.id(1L)
							.storeName("스프링 카페")
							.image(
								ImageRes.builder()
									.apiUrl("/images/")
									.filename("event_img.jpg")
									.build()
							)
							.build()
					)
					.build()
			)
			.build();
	}

	@Operation(summary = "북마크 취소")
	@DeleteMapping("/{id}")
	public Result<Void> deleteBookmark(@PathVariable Long id) {
		return Result.<Void>builder().build();
	}

	@Operation(summary = "북마크 폴더 생성")
	@PostMapping("/folders")
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

	@Operation(summary = "북마크 폴더 목록 조회")
	@GetMapping("/folders")
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
				.bookmarks(List.of(
					BookmarkRes.builder()
						.id(1L)
						.bookmarkedEventRes(
							BookmarkedEventRes.builder()
								.id(1L)
								.storeName("스프링 카페")
								.image(
									ImageRes.builder()
										.apiUrl("/images/")
										.filename("event_img.jpg")
										.build()
								)
								.build()
						)
						.build(),
					BookmarkRes.builder()
						.id(2L)
						.bookmarkedEventRes(
							BookmarkedEventRes.builder()
								.id(2L)
								.storeName("카페 언노운")
								.image(
									ImageRes.builder()
										.apiUrl("/images/")
										.filename("event_img.jpg")
										.build()
								)
								.build()
						)
						.build()
				))
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
				.bookmarks(List.of(
					BookmarkRes.builder()
						.id(3L)
						.bookmarkedEventRes(
							BookmarkedEventRes.builder()
								.id(3L)
								.storeName("곱창 파는 고깃집")
								.image(
									ImageRes.builder()
										.apiUrl("/images/")
										.filename("event_img.jpg")
										.build()
								)
								.build()
						)
						.build(),
					BookmarkRes.builder()
						.id(4L)
						.bookmarkedEventRes(
							BookmarkedEventRes.builder()
								.id(4L)
								.storeName("햬화 꽃집")
								.image(
									ImageRes.builder()
										.apiUrl("/images/")
										.filename("event_img.jpg")
										.build()
								)
								.build()
						)
						.build()
				))
				.build()
		));
	}

	@Operation(summary = "북마크 폴더 pk로 조회")
	@GetMapping("/folders/{id}")
	public Result<BookmarkFolderRes> getBookmarkFolder(@PathVariable Long id) {
		return Result.<BookmarkFolderRes>builder()
			.data(
				BookmarkFolderRes.builder()
					.id(id)
					.name("Folder1")
					.image(
						ImageRes.builder()
							.apiUrl("/images/")
							.filename("default_folder.jpg")
							.build()
					)
					.bookmarks(List.of(
						BookmarkRes.builder()
							.id(1L)
							.bookmarkedEventRes(
								BookmarkedEventRes.builder()
									.id(1L)
									.storeName("스프링 카페")
									.image(
										ImageRes.builder()
											.apiUrl("/images/")
											.filename("event_img.jpg")
											.build()
									)
									.build()
							)
							.build(),
						BookmarkRes.builder()
							.id(2L)
							.bookmarkedEventRes(
								BookmarkedEventRes.builder()
									.id(2L)
									.storeName("카페 언노운")
									.image(
										ImageRes.builder()
											.apiUrl("/images/")
											.filename("event_img.jpg")
											.build()
									)
									.build()
							)
							.build()
					))
					.build()
			)
			.build();
	}

	@Operation(summary = "북마크 폴더명 수정")
	@PutMapping("/folders/{id}")
	public Result<Void> updateBookmarkFolder(@PathVariable Long id,
		@Validated @RequestBody UpdateBookmarkFolderReq updateBookmarkFolderReq) {
		return Result.<Void>builder().build();
	}

	@Operation(summary = "북마크 폴더 삭제")
	@DeleteMapping("/folders/{id}")
	public Result<Void> deleteBookmarkFolder(@PathVariable Long id) {
		return Result.<Void>builder().build();
	}
}
