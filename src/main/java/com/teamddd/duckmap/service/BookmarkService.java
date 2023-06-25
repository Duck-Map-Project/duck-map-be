package com.teamddd.duckmap.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.bookmark.CreateBookmarkReq;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventBookmark;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.AuthenticationRequiredException;
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
	public Long createBookmark(Long eventId, CreateBookmarkReq createBookmarkReq, Member member) {
		Event event = eventService.getEvent(eventId);
		EventBookmarkFolder bookmarkFolder = bookmarkFolderService
			.getEventBookmarkFolder(createBookmarkReq.getBookmarkFolderId());
		EventBookmark bookmark = EventBookmark.builder()
			.member(member)
			.eventBookmarkFolder(bookmarkFolder)
			.event(event)
			.build();

		bookmarkRepository.save(bookmark);

		return bookmark.getId();
	}

	@Transactional
	public void deleteBookmark(Long id, Long loginMemberId) {
		EventBookmark bookmark = getEventBookmark(id);
		if (!loginMemberId.equals(bookmark.getMember().getId())) {
			throw new AuthenticationRequiredException();
		}
		bookmarkRepository.deleteById(id);
	}

	public EventBookmark getEventBookmark(Long bookmarkId) throws NonExistentBookmarkException {
		return bookmarkRepository.findById(bookmarkId)
			.orElseThrow(NonExistentBookmarkException::new);
	}
}
