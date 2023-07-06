package com.teamddd.duckmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamddd.duckmap.entity.EventLike;

public interface EventLikeRepository extends JpaRepository<EventLike, Long> {
	Long countByEventId(Long eventId);

	@Modifying(clearAutomatically = true)
	@Query("delete from EventLike el where el.member.id = :memberId")
	int deleteByMemberId(@Param("memberId") Long memberId);
}
