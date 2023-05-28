package com.teamddd.duckmap.repository;

import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderEventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkFolderRepositoryCustom {
	Page<BookmarkFolderEventDto> findBookmarkedEvents(Long bookmarkFolderId, Pageable pageable);
}
