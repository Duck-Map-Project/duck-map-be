package com.teamddd.duckmap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;

import com.teamddd.duckmap.dto.event.bookmark.BookmarkEventDto;

public interface BookmarkRepositoryCustom {
	@EntityGraph(attributePaths = {"event"}, type = EntityGraph.EntityGraphType.LOAD)
	Optional<BookmarkEventDto> findtByEventAndMember(Long eventId, Long memberId);
}
