package com.teamddd.duckmap.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.bookmark.CreateBookmarkReq;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventBookmark;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.Member;
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
}
