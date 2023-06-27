package com.teamddd.duckmap.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.bookmark.BookmarkEventDto;
import com.teamddd.duckmap.dto.event.bookmark.UpdateBookmarkReq;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventBookmark;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.NonExistentBookmarkException;
import com.teamddd.duckmap.repository.BookmarkRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {
	private final BookmarkRepository bookmarkRepository;
	private final EventService eventService;
	private final BookmarkFolderService bookmarkFolderService;

	@Transactional
	public Long createBookmark(Long eventId, Long bookmarkFolderId, Member member) {
		Event event = eventService.getEvent(eventId);
		EventBookmarkFolder bookmarkFolder = bookmarkFolderService
			.getEventBookmarkFolder(bookmarkFolderId);
		EventBookmark bookmark = EventBookmark.builder()
			.member(member)
			.eventBookmarkFolder(bookmarkFolder)
			.event(event)
			.build();

		bookmarkRepository.save(bookmark);

		return bookmark.getId();
	}

	@Transactional
	public void updateBookmark(Long eventId, UpdateBookmarkReq updateBookmarkReq, Member member)
		throws NonExistentBookmarkException {
		EventBookmarkFolder bookmarkFolder = bookmarkFolderService
			.getEventBookmarkFolder(updateBookmarkReq.getBookmarkFolderId());
		EventBookmark bookmark = getEventBookmark(eventId, member.getId());
		bookmark.updateEventBookmark(bookmarkFolder);
	}

	@Transactional
	public void deleteBookmark(Long eventId, Long loginMemberId) {
		EventBookmark bookmark = getEventBookmark(eventId, loginMemberId);
		bookmarkRepository.deleteById(bookmark.getId());
	}

	public EventBookmark getEventBookmark(Long eventId, Long loginMemberId) throws NonExistentBookmarkException {
		BookmarkEventDto bookmarkEventDto = bookmarkRepository
			.findtByEventAndMember(eventId, loginMemberId)
			.orElseThrow(NonExistentBookmarkException::new);
		return bookmarkEventDto.getEventBookmark();
	}
}
