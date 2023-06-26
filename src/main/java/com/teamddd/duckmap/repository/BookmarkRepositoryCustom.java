package com.teamddd.duckmap.repository;

import java.util.Optional;

import com.teamddd.duckmap.dto.event.bookmark.BookmarkEventDto;

public interface BookmarkRepositoryCustom {
	Optional<BookmarkEventDto> findByIdWithEvent(Long eventId, Long memberId);
}
