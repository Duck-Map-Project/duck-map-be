package com.teamddd.duckmap.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventLike;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.AuthenticationRequiredException;
import com.teamddd.duckmap.exception.NonExistentMemberException;
import com.teamddd.duckmap.repository.EventLikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventLikeService {
	private final EventService eventService;
	private final EventLikeRepository eventLikeRepository;

	@Transactional
	public EventLike likeEvent(Long eventId, Member member) {
		Event event = eventService.getEvent(eventId);

		EventLike eventLike = EventLike.builder()
			.event(event)
			.member(member)
			.build();

		eventLikeRepository.save(eventLike);

		return eventLike;
	}

	@Transactional
	public void deleteLikeEvent(Long id, Member loginMember) {
		Member member = getMember(id);
		if (!member.equals(loginMember)) {
			throw new AuthenticationRequiredException();
		}
		eventLikeRepository.deleteById(id);
	}

	public Member getMember(Long likeId) throws NonExistentMemberException {
		return eventLikeRepository.getMemberById(likeId)
			.orElseThrow(NonExistentMemberException::new);
	}
}
