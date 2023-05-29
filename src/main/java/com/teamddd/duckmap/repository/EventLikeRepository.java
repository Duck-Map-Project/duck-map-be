package com.teamddd.duckmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamddd.duckmap.entity.EventLike;

public interface EventLikeRepository extends JpaRepository<EventLike, Long> {
	Integer countByEventId(Long eventId);

}
