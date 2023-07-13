package com.teamddd.duckmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamddd.duckmap.entity.EventImage;

public interface EventImageRepository extends JpaRepository<EventImage, Long> {
	@Modifying
	@Query("delete from EventImage ei where ei.event.id = :eventId")
	int deleteByEventId(@Param("eventId") Long eventId);
}
