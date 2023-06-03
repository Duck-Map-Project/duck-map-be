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

import com.teamddd.duckmap.dto.event.bookmark.CreateBookmarkReq;
import com.teamddd.duckmap.dto.event.bookmark.CreateBookmarkRes;
import com.teamddd.duckmap.dto.event.bookmark.UpdateBookmarkReq;

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
	public CreateBookmarkRes createBookmark(@PathVariable Long id, HttpSession session,
		@Validated @RequestBody
		CreateBookmarkReq createBookmarkReq) {
		return CreateBookmarkRes.builder()
			.id(1L)
			.build();
	}

	@Operation(summary = "북마크 폴더 변경")
	@PutMapping("/{id}/bookmarks")
	public void updateBookmark(@PathVariable Long id,
		@Validated @RequestBody UpdateBookmarkReq updateBookmarkReq) {
	}

	@Operation(summary = "북마크 취소")
	@DeleteMapping("/{id}/bookmarks")
	public void deleteBookmark(@PathVariable Long id) {
	}

}
