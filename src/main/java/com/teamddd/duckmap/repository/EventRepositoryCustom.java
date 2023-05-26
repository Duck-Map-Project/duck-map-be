package com.teamddd.duckmap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.teamddd.duckmap.dto.event.event.EventLikeBookmarkDto;

public interface EventRepositoryCustom {

	Page<EventLikeBookmarkDto> findMyEvents(Long userId, Pageable pageable);
}
