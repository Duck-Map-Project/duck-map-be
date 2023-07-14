package com.teamddd.duckmap.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.teamddd.duckmap.dto.event.event.EventLikeBookmarkDto;
import com.teamddd.duckmap.dto.event.event.EventLikeReviewCountDto;

public interface EventRepositoryCustom {

	Optional<EventLikeBookmarkDto> findByIdWithLikeAndBookmark(Long eventId, Long userId);

	Page<EventLikeBookmarkDto> findMyEvents(Long memberId, Pageable pageable);

	Page<EventLikeBookmarkDto> findMyLikeEvents(Long memberId, Pageable pageable);

	Page<EventLikeBookmarkDto> findByArtistAndDate(Long artistId, LocalDate date, Long memberId, Pageable pageable);

	Page<EventLikeReviewCountDto> findForMap(LocalDate date, Pageable pageable);
}
