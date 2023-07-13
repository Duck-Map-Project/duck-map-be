package com.teamddd.duckmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamddd.duckmap.entity.EventCategory;
import com.teamddd.duckmap.entity.EventInfoCategory;

public interface EventInfoCategoryRepository extends JpaRepository<EventInfoCategory, Long> {
	Long countByEventCategory(EventCategory eventCategory);

	@Modifying
	@Query("delete from EventInfoCategory eic where eic.event.id = :eventId")
	int deleteByEventId(@Param("eventId") Long eventId);
}
