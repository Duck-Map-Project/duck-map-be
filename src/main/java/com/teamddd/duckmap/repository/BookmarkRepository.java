package com.teamddd.duckmap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamddd.duckmap.entity.EventBookmark;

public interface BookmarkRepository extends JpaRepository<EventBookmark, Long> {
	@EntityGraph(attributePaths = {"event"})
	Optional<EventBookmark> findByEventIdAndMemberId(Long eventId, Long memberId);

	@Modifying(clearAutomatically = true)
	@Query("delete from EventBookmark eb where eb.eventBookmarkFolder.id = :bookmarkFolderId")
	int deleteByBookmarkFolderId(@Param("bookmarkFolderId") Long bookmarkFolderId);
}
