package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.category.CreateEventCategoryReq;
import com.teamddd.duckmap.dto.event.category.EventCategoryRes;
import com.teamddd.duckmap.entity.EventCategory;
import com.teamddd.duckmap.repository.EventCategoryRepository;

@Transactional
@SpringBootTest
class EventCategoryServiceTest {
	@Autowired
	EventCategoryService eventCategoryService;
	@SpyBean
	EventCategoryRepository eventCategoryRepository;

	@DisplayName("이벤트 카테고리를 생성한다")
	@Test
	void createEventCategory() throws Exception {
		//given
		CreateEventCategoryReq request = new CreateEventCategoryReq();
		ReflectionTestUtils.setField(request, "category", "category1");

		//when
		Long eventCategoryId = eventCategoryService.createEventCategory(request);

		//then
		assertThat(eventCategoryId).isNotNull();

		Optional<EventCategory> findCategory = eventCategoryRepository.findById(eventCategoryId);
		assertThat(findCategory).isNotEmpty();
		assertThat(findCategory.get())
			.extracting("category")
			.isEqualTo("category1");
	}

	@DisplayName("이벤트 카테고리 전체 목록을 조회한다")
	@Test
	void getEventCategoryResList() throws Exception {
		//given
		EventCategory category1 = createEventCategory("category1");
		EventCategory category2 = createEventCategory("category2");
		EventCategory category3 = createEventCategory("category3");
		eventCategoryRepository.saveAll(List.of(category1, category2, category3));

		//when
		List<EventCategoryRes> eventCategoryResList = eventCategoryService.getEventCategoryResList();

		//then
		assertThat(eventCategoryResList).hasSize(3)
			.extracting("category")
			.containsExactlyInAnyOrder("category1", "category2", "category3");
	}

	EventCategory createEventCategory(String category) {
		return EventCategory.builder()
			.category(category)
			.build();
	}

}
