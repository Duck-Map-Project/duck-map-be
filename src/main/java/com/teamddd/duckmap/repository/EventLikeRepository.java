package com.teamddd.duckmap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamddd.duckmap.entity.EventLike;
import com.teamddd.duckmap.entity.Member;

public interface EventLikeRepository extends JpaRepository<EventLike, Long> {
	Long countByEventId(Long eventId);

	@Query("select el.member from EventLike el where el.id = :id")
	Optional<Member> findMemberById(@Param("id") Long id);
}
