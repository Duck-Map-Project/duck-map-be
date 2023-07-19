package com.teamddd.duckmap.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.teamddd.duckmap.common.ApiUrl;
import com.teamddd.duckmap.common.Props;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkEventDto;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderMemberDto;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderMemberRes;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderRes;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkedEventRes;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkedEventsServiceReq;
import com.teamddd.duckmap.dto.event.bookmark.CreateBookmarkFolderReq;
import com.teamddd.duckmap.dto.event.bookmark.MyBookmarkFolderServiceReq;
import com.teamddd.duckmap.dto.event.bookmark.UpdateBookmarkFolderReq;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.NonExistentBookmarkFolderException;
import com.teamddd.duckmap.repository.BookmarkFolderRepository;
import com.teamddd.duckmap.repository.BookmarkRepository;
import com.teamddd.duckmap.util.FileUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkFolderService {
	private final Props props;
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
		EventBookmarkFolder bookmarkFolder = bookmarkFolderRepository.findById(bookmarkFolderId)
			.orElseThrow(NonExistentBookmarkFolderException::new);

		//기존 북마크 폴더 image
		String oldImage = bookmarkFolder.getImage();

		bookmarkFolder.updateEventBookmarkFolder(request.getName(), request.getImage(), request.getColor());

		//북마크 폴더 image가 변경되었다면 서버에서 기존 북마크 폴더 image 삭제
		if (StringUtils.hasText(oldImage) && !oldImage.equals(request.getImage())) {
			FileUtils.deleteFile(props.getImageDir(), oldImage);
		}
	}

	@Transactional
	public void deleteBookmarkFolder(Long bookmarkFolderId) {
		EventBookmarkFolder bookmarkFolder = bookmarkFolderRepository.findById(bookmarkFolderId)
			.orElseThrow(NonExistentBookmarkFolderException::new);

		//관련 북마크 삭제
		bookmarkRepository.deleteByBookmarkFolderId(bookmarkFolderId);

		//서버에서 북마크폴더 이미지 삭제
		if (StringUtils.hasText(bookmarkFolder.getImage())) {
			FileUtils.deleteFile(props.getImageDir(), bookmarkFolder.getImage());
		}
		bookmarkFolderRepository.deleteById(bookmarkFolderId);
	}

	public BookmarkFolderMemberRes getBookmarkFolderMemberRes(Long bookmarkFolderId) {
		BookmarkFolderMemberDto bookmarkFolderMemberDto = bookmarkFolderRepository
			.findBookmarkFolderAndMemberById(bookmarkFolderId)
			.orElseThrow(NonExistentBookmarkFolderException::new);

		return BookmarkFolderMemberRes.builder()
			.id(bookmarkFolderId)
			.name(bookmarkFolderMemberDto.getBookmarkFolder().getName())
			.image(ApiUrl.IMAGE + bookmarkFolderMemberDto.getBookmarkFolder().getImage())
			.color(bookmarkFolderMemberDto.getBookmarkFolder().getColor())
			.memberId(bookmarkFolderMemberDto.getMemberId())
			.username(bookmarkFolderMemberDto.getUsername())
			.color(bookmarkFolderMemberDto.getBookmarkFolder().getColor())
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

	public Page<BookmarkedEventRes> getBookmarkedEventResList(BookmarkedEventsServiceReq request) {
		Page<BookmarkEventDto> bookmarkedEvents = bookmarkFolderRepository.findBookmarkedEvents(
			request.getBookmarkFolderId(),
			request.getPageable());
		return bookmarkedEvents.map(BookmarkedEventRes::of);
	}
}
