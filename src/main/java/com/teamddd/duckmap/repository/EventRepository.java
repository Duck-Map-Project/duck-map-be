package com.teamddd.duckmap.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamddd.duckmap.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {

	@Query("select DISTINCT e from Event e join EventArtist ea on e = ea.event join ea.artist a "
		+ "where e.fromDate <= :date and e.toDate >= :date")
	List<Event> findByArtistAndDate(@Param("date") LocalDate date);
}
