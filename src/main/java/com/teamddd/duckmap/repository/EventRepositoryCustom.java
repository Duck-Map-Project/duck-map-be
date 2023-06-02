package com.teamddd.duckmap.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.teamddd.duckmap.dto.event.event.EventLikeBookmarkDto;

public interface EventRepositoryCustom {

	Optional<EventLikeBookmarkDto> findByIdWithLikeAndBookmark(Long eventId, Long userId);

	Page<EventLikeBookmarkDto> findMyEvents(Long userId, Pageable pageable);

	Page<EventLikeBookmarkDto> findMyLikeEvents(Long userId, Pageable pageable);

	Page<EventLikeBookmarkDto> findByArtistAndDate(Long artistId, LocalDate date, Long userId, Pageable pageable);
}
