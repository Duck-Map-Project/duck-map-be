package com.teamddd.duckmap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderEventDto;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderUserDto;
import com.teamddd.duckmap.entity.EventBookmarkFolder;

public interface BookmarkFolderRepositoryCustom {
	Page<BookmarkFolderEventDto> findBookmarkedEvents(Long bookmarkFolderId, Pageable pageable);

	Page<EventBookmarkFolder> findBookmarkFoldersByUserId(Long userId, Pageable pageable);

	BookmarkFolderUserDto findBookmarkFolderAndUserById(Long bookmarkFolderId);

}
