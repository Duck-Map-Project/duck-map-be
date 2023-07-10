package com.teamddd.duckmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamddd.duckmap.entity.EventCategory;
import com.teamddd.duckmap.entity.EventInfoCategory;

public interface EventInfoCategoryRepository extends JpaRepository<EventInfoCategory, Long> {
	Long countByEventCategory(EventCategory eventCategory);
}
