package com.teamddd.duckmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.EventArtist;

@Transactional
public interface EventArtistRepository extends JpaRepository<EventArtist, Long> {

	@Modifying
	@Query("delete from EventArtist ea where ea.event.id = :eventId")
	int deleteByEventId(@Param("eventId") Long eventId);

	@Modifying(clearAutomatically = true)
	@Query("update EventArtist ea set ea.artist = null where ea.artist.id = :artistId")
	int updateArtistToNull(@Param("artistId") Long artistId);
}
