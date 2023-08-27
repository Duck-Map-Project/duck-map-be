package com.teamddd.duckmap.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.category.CreateEventCategoryReq;
import com.teamddd.duckmap.dto.event.category.EventCategoryRes;
import com.teamddd.duckmap.dto.event.category.UpdateEventCategoryServiceReq;
import com.teamddd.duckmap.entity.EventCategory;
import com.teamddd.duckmap.exception.NonExistentEventCategoryException;
import com.teamddd.duckmap.exception.UnableToDeleteEventCategoryInUseException;
import com.teamddd.duckmap.repository.EventCategoryRepository;
import com.teamddd.duckmap.repository.EventInfoCategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventCategoryService {
	private final EventCategoryRepository eventCategoryRepository;
	private final EventInfoCategoryRepository eventInfoCategoryRepository;

	@Transactional
	public Long createEventCategory(CreateEventCategoryReq createEventCategoryReq) {
		EventCategory eventCategory = EventCategory.builder()
			.category(createEventCategoryReq.getCategory())
			.build();

		eventCategoryRepository.save(eventCategory);

		return eventCategory.getId();
	}

	public EventCategory getEventCategory(Long id) {
		return eventCategoryRepository.findById(id)
			.orElseThrow(NonExistentEventCategoryException::new);
	}

	public List<EventCategory> getEventCategoriesByIds(List<Long> ids) {
		List<EventCategory> categories = eventCategoryRepository.findByIdIn(ids);

		Set<Long> duplicatedIds = new HashSet<>(ids);
		if (duplicatedIds.size() == categories.size()) {
			return categories;
		}
		throw new NonExistentEventCategoryException();
	}

	public List<EventCategoryRes> getEventCategoryResList() {
		List<EventCategory> categories = eventCategoryRepository.findAll();
		return categories.stream()
			.map(EventCategoryRes::of)
			.toList();
	}

	@Transactional
	public void updateEventCategory(UpdateEventCategoryServiceReq request) {
		EventCategory eventCategory = getEventCategory(request.getId());

		eventCategory.updateEventCategory(request.getCategory());
	}

	@Transactional
	public void deleteEventCategory(Long id) {
		EventCategory eventCategory = getEventCategory(id);

		Long eventCount = eventInfoCategoryRepository.countByEventCategory(eventCategory);
		if (eventCount > 0) {
			throw new UnableToDeleteEventCategoryInUseException();
		}

		eventCategoryRepository.delete(eventCategory);
	}
}
