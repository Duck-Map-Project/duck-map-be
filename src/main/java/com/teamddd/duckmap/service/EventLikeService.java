package com.teamddd.duckmap.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventLike;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.repository.EventLikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventLikeService {
	private final EventService eventService;
	private final EventLikeRepository eventLikeRepository;

	@Transactional
	public Long likeEvent(Long eventId, Member member) {
		Event event = eventService.getEvent(eventId);

		EventLike eventLike = EventLike.builder()
			.event(event)
			.member(member)
			.build();

		eventLikeRepository.save(eventLike);

		return eventLike.getId();
	}

	@Transactional
	public void deleteLikeEvent(Long id) {
		eventLikeRepository.deleteById(id);
	}
}
