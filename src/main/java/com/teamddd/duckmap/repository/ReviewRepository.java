package com.teamddd.duckmap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.teamddd.duckmap.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	Page<Review> findByUserId(Long userId, Pageable pageable);
}
