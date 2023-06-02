package com.teamddd.duckmap.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.teamddd.duckmap.entity.Review;

public interface ReviewRepositoryCustom {
	Page<Review> findByArtistAndDate(Long artistId, LocalDate date, Pageable pageable);
}
