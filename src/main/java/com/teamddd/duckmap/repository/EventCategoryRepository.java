package com.teamddd.duckmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamddd.duckmap.entity.EventCategory;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
}
