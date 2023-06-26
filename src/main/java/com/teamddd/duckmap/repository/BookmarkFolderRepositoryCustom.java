package com.teamddd.duckmap.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.teamddd.duckmap.dto.event.bookmark.BookmarkEventDto;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderMemberDto;
import com.teamddd.duckmap.entity.EventBookmarkFolder;

public interface BookmarkFolderRepositoryCustom {
	Page<BookmarkEventDto> findBookmarkedEvents(Long bookmarkFolderId, Pageable pageable);

	Page<EventBookmarkFolder> findBookmarkFoldersByMemberId(Long memberId, Pageable pageable);

	Optional<BookmarkFolderMemberDto> findBookmarkFolderAndMemberById(Long bookmarkFolderId);

}
