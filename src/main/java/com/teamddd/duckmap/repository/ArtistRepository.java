package com.teamddd.duckmap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.ArtistType;

public interface ArtistRepository extends JpaRepository<Artist, Long>, ArtistRepositoryCustom {

	List<Artist> findByGroup(Artist group);

	List<Artist> findByIdIn(List<Long> id);

	Long countByArtistType(ArtistType artistType);

	@Modifying(clearAutomatically = true)
	@Query("update Artist a set a.group = null where a.group.id = :groupId")
	int updateGroupToNull(@Param("groupId") Long groupId);
}
