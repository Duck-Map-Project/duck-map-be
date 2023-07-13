package com.teamddd.duckmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamddd.duckmap.entity.EventBookmark;

public interface EventBookmarkRepository extends JpaRepository<EventBookmark, Long> {
	@Modifying
	@Query("delete from EventBookmark eb where eb.event.id = :eventId")
	int deleteByEventId(@Param("eventId") Long eventId);
}
