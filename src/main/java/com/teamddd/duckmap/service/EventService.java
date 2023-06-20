package com.teamddd.duckmap.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.exception.NonExistentEventException;
import com.teamddd.duckmap.repository.EventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {
	private final EventRepository eventRepository;

	public Event getEvent(Long eventId) throws NonExistentEventException {
		return eventRepository.findById(eventId)
			.orElseThrow(NonExistentEventException::new);
	}
}
