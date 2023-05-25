package com.teamddd.duckmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamddd.duckmap.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
