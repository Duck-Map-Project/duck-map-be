package com.teamddd.duckmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamddd.duckmap.entity.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Long>, ArtistRepositoryCustom {
}
