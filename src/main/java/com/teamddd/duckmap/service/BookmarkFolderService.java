package com.teamddd.duckmap.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.bookmark.BookmarkEventDto;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderMemberDto;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderMemberRes;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderRes;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkedEventRes;
import com.teamddd.duckmap.dto.event.bookmark.CreateBookmarkFolderReq;
import com.teamddd.duckmap.dto.event.bookmark.MyBookmarkFolderServiceReq;
import com.teamddd.duckmap.dto.event.bookmark.UpdateBookmarkFolderReq;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.NonExistentBookmarkFolderException;
import com.teamddd.duckmap.repository.BookmarkFolderRepository;
import com.teamddd.duckmap.repository.BookmarkRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkFolderService {
	private final BookmarkFolderRepository bookmarkFolderRepository;
	private final BookmarkRepository bookmarkRepository;

	@Transactional
	public Long createBookmarkFolder(CreateBookmarkFolderReq request, Member member) {

		EventBookmarkFolder bookmarkFolder = EventBookmarkFolder.builder()
			.member(member)
			.name(request.getName())
			.image(request.getImage())
			.color(request.getColor())
			.build();

		bookmarkFolderRepository.save(bookmarkFolder);

		return bookmarkFolder.getId();
	}

	@Transactional
	public void updateBookmarkFolder(Long bookmarkFolderId, UpdateBookmarkFolderReq request) {
		EventBookmarkFolder bookmarkFolder = getEventBookmarkFolder(bookmarkFolderId);
		bookmarkFolder.updateEventBookmarkFolder(request.getName(), request.getImage(), request.getColor());
	}

	@Transactional
	public void deleteBookmarkFolder(Long bookmarkFolderId) {
		EventBookmarkFolder bookmarkFolder = getEventBookmarkFolder(bookmarkFolderId);

		//관련 북마크 삭제
		bookmarkRepository.deleteByBookmarkFolderId(bookmarkFolderId);

		bookmarkFolderRepository.deleteById(bookmarkFolderId);
	}

	public BookmarkFolderMemberRes getBookmarkFolderMemberRes(Long bookmarkFolderId) {
		BookmarkFolderMemberDto bookmarkFolderMemberDto = bookmarkFolderRepository
			.findBookmarkFolderAndMemberById(bookmarkFolderId)
			.orElseThrow(NonExistentBookmarkFolderException::new);

		//북마크된 이벤트 페이지를 가져온다.
		Pageable pageable = PageRequest.of(0, 20);
		Page<BookmarkedEventRes> bookmarkedEventResPage = bookmarkFolderRepository.findBookmarkedEvents(
				bookmarkFolderId, pageable)
			.map(BookmarkedEventRes::of);

		return BookmarkFolderMemberRes.builder()
			.id(bookmarkFolderId)
			.name(bookmarkFolderMemberDto.getBookmarkFolder().getName())
			.memberId(bookmarkFolderMemberDto.getMemberId())
			.username(bookmarkFolderMemberDto.getUsername())
			.bookmarkedEventResPage(bookmarkedEventResPage)
			.build();
	}

	public EventBookmarkFolder getEventBookmarkFolder(Long bookmarkFolderId) throws NonExistentBookmarkFolderException {
		return bookmarkFolderRepository.findById(bookmarkFolderId)
			.orElseThrow(NonExistentBookmarkFolderException::new);
	}

	public Page<BookmarkFolderRes> getMyBookmarkFolderResList(MyBookmarkFolderServiceReq request) {
		Page<EventBookmarkFolder> myBookmarkFolders = bookmarkFolderRepository.findBookmarkFoldersByMemberId(
			request.getMemberId(),
			request.getPageable());
		return myBookmarkFolders.map(BookmarkFolderRes::of);
	}

	public Page<BookmarkedEventRes> getBookmarkedEventResList(Long bookmarkFolderId, Pageable pageable) {
		Page<BookmarkEventDto> bookmarkedEvents = bookmarkFolderRepository.findBookmarkedEvents(
			bookmarkFolderId,
			pageable);
		return bookmarkedEvents.map(BookmarkedEventRes::of);
	}
}
