package com.teamddd.duckmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamddd.duckmap.entity.EventBookmark;

public interface BookmarkRepository extends JpaRepository<EventBookmark, Long> {
}
