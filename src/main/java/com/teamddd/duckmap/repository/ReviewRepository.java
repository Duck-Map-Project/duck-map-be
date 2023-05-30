package com.teamddd.duckmap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.Review;
import com.teamddd.duckmap.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	Page<Review> findByUser(User user, Pageable pageable);

	Page<Review> findByEvent(Event event, Pageable pageable);
}
