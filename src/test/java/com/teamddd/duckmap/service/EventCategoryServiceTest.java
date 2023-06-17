package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.category.CreateEventCategoryReq;
import com.teamddd.duckmap.dto.event.category.EventCategoryRes;
import com.teamddd.duckmap.entity.EventCategory;
import com.teamddd.duckmap.exception.NonExistentEventCategoryException;
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

	@DisplayName("이벤트 카테고리 id 목록으로 카테고리 목록을 조회한다")
	@Nested
	class GetEventCategoriesByIds {
		@DisplayName("유효한 id로만 조회한 경우")
		@Test
		void getEventCategoriesByIds1() throws Exception {
			//given
			EventCategory category1 = createEventCategory("category1");
			EventCategory category2 = createEventCategory("category2");
			EventCategory category3 = createEventCategory("category3");
			EventCategory category4 = createEventCategory("category3");
			eventCategoryRepository.saveAll(List.of(category1, category2, category3, category4));

			List<Long> inIds = List.of(category1.getId(), category3.getId());

			//when
			List<EventCategory> findCategories = eventCategoryService.getEventCategoriesByIds(inIds);

			//then
			assertThat(findCategories).hasSize(2)
				.extracting("category")
				.containsExactlyInAnyOrder("category1", "category3");
		}

		@DisplayName("유효하지 않은 id를 포함하여 조회한 경우")
		@Test
		void getEventCategoriesByIds2() throws Exception {
			//given
			EventCategory category1 = createEventCategory("category1");
			EventCategory category2 = createEventCategory("category2");
			EventCategory category3 = createEventCategory("category3");
			eventCategoryRepository.saveAll(List.of(category1, category2, category3));

			List<Long> inIds = List.of(category1.getId(), 100L);

			//when //then
			assertThatThrownBy(() -> eventCategoryService.getEventCategoriesByIds(inIds))
				.isInstanceOf(NonExistentEventCategoryException.class);
		}
	}
}
