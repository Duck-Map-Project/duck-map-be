package com.teamddd.duckmap.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderMemberDto;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderMemberRes;
import com.teamddd.duckmap.dto.event.bookmark.CreateBookmarkFolderReq;
import com.teamddd.duckmap.dto.event.bookmark.UpdateBookmarkFolderReq;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.NonExistentBookmarkFolderException;
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

	@Transactional
	public void updateBookmarkFolder(Long bookmarkFolderId, UpdateBookmarkFolderReq updateBookmarkFolderReq) {
		EventBookmarkFolder bookmarkFolder = bookmarkFolderRepository.findById(bookmarkFolderId)
			.orElseThrow(NonExistentBookmarkFolderException::new);
		bookmarkFolder.updateEventBookmarkFolder(updateBookmarkFolderReq.getName(), updateBookmarkFolderReq.getImage());

	}

	public BookmarkFolderMemberRes getBookmarkFolderMemberRes(Long bookmarkFolderId) {
		BookmarkFolderMemberDto bookmarkFolderMemberDto = bookmarkFolderRepository
			.findBookmarkFolderAndMemberById(bookmarkFolderId);

		if (bookmarkFolderMemberDto == null) {
			throw new NonExistentBookmarkFolderException();
		}

		return BookmarkFolderMemberRes.builder()
			.id(bookmarkFolderId)
			.name(bookmarkFolderMemberDto.getBookmarkFolder().getName())
			.image(
				ImageRes.builder()
					.filename(bookmarkFolderMemberDto.getBookmarkFolder().getImage())
					.build()
			)
			.memberId(bookmarkFolderMemberDto.getMemberId())
			.username(bookmarkFolderMemberDto.getUsername())
			.build();
	}
}
