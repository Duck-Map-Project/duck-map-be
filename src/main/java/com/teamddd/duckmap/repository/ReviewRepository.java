package com.teamddd.duckmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamddd.duckmap.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
