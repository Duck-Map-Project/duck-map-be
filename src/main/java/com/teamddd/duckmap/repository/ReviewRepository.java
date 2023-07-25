package com.teamddd.duckmap.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
	Page<Review> findByMember(Member member, Pageable pageable);

	Page<Review> findByEvent(Event event, Pageable pageable);

	Page<Review> findByEventId(Long eventId, Pageable pageable);

	@Query("select ROUND(avg(r.score), 1) from Review r where r.event.id = :eventId")
	Optional<Double> avgScoreByEvent(@Param("eventId") Long eventId);

	@Modifying(clearAutomatically = true)
	@Query("update Review r set r.event = null where r.event.id = :eventId")
	int updateEventToNull(@Param("eventId") Long eventId);
}
