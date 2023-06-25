package com.teamddd.duckmap.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.bookmark.CreateBookmarkFolderReq;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.repository.BookmarkFolderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkFolderService {
	private final BookmarkFolderRepository bookmarkFolderRepository;

	@Transactional
	public Long createBookmarkFolder(CreateBookmarkFolderReq createBookmarkFolderReq, Member member) {

		EventBookmarkFolder bookmarkFolder = EventBookmarkFolder.builder()
			.member(member)
			.name(createBookmarkFolderReq.getName())
			.image(createBookmarkFolderReq.getImage())
			.build();

		bookmarkFolderRepository.save(bookmarkFolder);

		return bookmarkFolder.getId();
	}
}
