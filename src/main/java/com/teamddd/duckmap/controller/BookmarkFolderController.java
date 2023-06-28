package com.teamddd.duckmap.controller;

import org.springframework.data.domain.Page;
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

import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderMemberRes;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderRes;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkedEventRes;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkedEventsServiceReq;
import com.teamddd.duckmap.dto.event.bookmark.CreateBookmarkFolderReq;
import com.teamddd.duckmap.dto.event.bookmark.CreateBookmarkFolderRes;
import com.teamddd.duckmap.dto.event.bookmark.MyBookmarkFolderServiceReq;
import com.teamddd.duckmap.dto.event.bookmark.UpdateBookmarkFolderReq;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.service.BookmarkFolderService;
import com.teamddd.duckmap.util.MemberUtils;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark-folders")
public class BookmarkFolderController {
	private final BookmarkFolderService bookmarkFolderService;

	@Operation(summary = "북마크 폴더 생성")
	@PostMapping
	public CreateBookmarkFolderRes createBookmarkFolder(
		@Validated @RequestBody CreateBookmarkFolderReq createBookmarkFolderReq) {
		Member member = MemberUtils.getAuthMember().getUser();
		Long bookmarkFolderId = bookmarkFolderService.createBookmarkFolder(createBookmarkFolderReq, member);
		return CreateBookmarkFolderRes.builder()
			.id(bookmarkFolderId)
			.build();
	}

	@Operation(summary = "로그인한 회원이 생성한 북마크 폴더 목록 조회")
	@GetMapping
	public Page<BookmarkFolderRes> getMyBookmarkFolders(Pageable pageable) {
		Member member = MemberUtils.getAuthMember().getUser();
		MyBookmarkFolderServiceReq request = MyBookmarkFolderServiceReq.builder()
			.memberId(member.getId())
			.pageable(pageable)
			.build();

		return bookmarkFolderService.getMyBookmarkFolderResList(request);
	}

	@Operation(summary = "북마크 폴더 pk로 북마크 폴더,사용자 정보 조회", description = "북마크 폴더 외부 공유용 api")
	@GetMapping("/{id}")
	public BookmarkFolderMemberRes getBookmarkFolder(@PathVariable Long id) {
		return bookmarkFolderService.getBookmarkFolderMemberRes(id);
	}

	@Operation(summary = "북마크 폴더 내부의 이벤트 목록 조회")
	@GetMapping("/{id}/events")
	public Page<BookmarkedEventRes> getAllBookmarks(@PathVariable Long id, Pageable pageable) {
		BookmarkedEventsServiceReq request = BookmarkedEventsServiceReq.builder()
			.bookmarkFolderId(id)
			.pageable(pageable)
			.build();
		return bookmarkFolderService.getBookmarkedEventResList(request);
	}

	@Operation(summary = "북마크 폴더명, 이미지 변경 요청")
	@PutMapping("/{id}")
	public void updateBookmarkFolder(@PathVariable Long id,
		@Validated @RequestBody UpdateBookmarkFolderReq updateBookmarkFolderReq) {
		bookmarkFolderService.updateBookmarkFolder(id, updateBookmarkFolderReq);
	}

	@Operation(summary = "북마크 폴더 삭제")
	@DeleteMapping("/{id}")
	public void deleteBookmarkFolder(@PathVariable Long id) {

	}
}
