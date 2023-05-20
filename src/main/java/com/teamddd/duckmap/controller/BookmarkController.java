package com.teamddd.duckmap.controller;

import javax.servlet.http.HttpSession;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.dto.CreateBookmarkReq;
import com.teamddd.duckmap.dto.CreateBookmarkRes;
import com.teamddd.duckmap.dto.Result;
import com.teamddd.duckmap.dto.UpdateBookmarkReq;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class BookmarkController {

	@Operation(summary = "북마크 생성")
	@PostMapping("/{id}/bookmarks")
	public Result<CreateBookmarkRes> createBookmark(@PathVariable Long id, HttpSession session,
		@Validated @RequestBody
		CreateBookmarkReq createBookmarkReq) {
		return Result.<CreateBookmarkRes>builder()
			.data(
				CreateBookmarkRes.builder()
					.id(1L)
					.build()
			)
			.build();
	}

	@Operation(summary = "북마크 폴더 변경")
	@PutMapping("/{id}/bookmarks")
	public Result<Void> updateBookmark(@PathVariable Long id,
		@Validated @RequestBody UpdateBookmarkReq UpdateBookmarkReq) {
		return Result.<Void>builder().build();
	}

	@Operation(summary = "북마크 취소")
	@DeleteMapping("/{id}/bookmarks")
	public Result<Void> deleteBookmark(@PathVariable Long id) {
		return Result.<Void>builder().build();
	}

}
