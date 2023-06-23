package com.teamddd.duckmap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamddd.duckmap.entity.EventLike;
import com.teamddd.duckmap.entity.Member;

public interface EventLikeRepository extends JpaRepository<EventLike, Long> {
	Long countByEventId(Long eventId);

	Optional<Member> getMemberById(Long likeId);
}
