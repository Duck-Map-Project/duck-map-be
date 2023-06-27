package com.teamddd.duckmap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.teamddd.duckmap.entity.EventBookmark;

public interface BookmarkRepository extends JpaRepository<EventBookmark, Long> {
	@EntityGraph(attributePaths = {"event"})
	Optional<EventBookmark> findByEventIdAndMemberId(Long eventId, Long memberId);
}
