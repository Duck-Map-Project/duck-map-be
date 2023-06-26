package com.teamddd.duckmap.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.bookmark.UpdateBookmarkReq;
import com.teamddd.duckmap.dto.event.event.EventLikeBookmarkDto;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventBookmark;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.NonExistentBookmarkException;
import com.teamddd.duckmap.exception.NonExistentEventException;
import com.teamddd.duckmap.repository.BookmarkRepository;
import com.teamddd.duckmap.repository.EventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {
	private final BookmarkRepository bookmarkRepository;
	private final EventRepository eventRepository;
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
		EventLikeBookmarkDto eventLikeBookmarkDto = eventRepository
			.findByIdWithLikeAndBookmark(eventId, member.getId())
			.orElseThrow(NonExistentEventException::new);
		EventBookmark bookmark = eventLikeBookmarkDto.getBookmark();
		if (bookmark == null) {
			throw new NonExistentBookmarkException();
		}
		bookmark.updateEventBookmark(bookmarkFolder);
	}

	@Transactional
	public void deleteBookmark(Long eventId, Long loginMemberId) {
		EventBookmark bookmark = getEventBookmark(eventId, loginMemberId);

		bookmarkRepository.deleteById(bookmark.getId());
	}

	public EventBookmark getEventBookmark(Long eventId, Long loginMemberId) throws NonExistentBookmarkException {
		EventLikeBookmarkDto eventLikeBookmarkDto = eventRepository
			.findByIdWithLikeAndBookmark(eventId, loginMemberId)
			.orElseThrow(NonExistentEventException::new);
		EventBookmark bookmark = eventLikeBookmarkDto.getBookmark();
		if (bookmark == null) {
			throw new NonExistentBookmarkException();
		}
		return bookmark;
	}
}
