package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.category.CreateEventCategoryReq;
import com.teamddd.duckmap.dto.event.category.EventCategoryRes;
import com.teamddd.duckmap.dto.event.category.UpdateEventCategoryServiceReq;
import com.teamddd.duckmap.entity.EventCategory;
import com.teamddd.duckmap.exception.NonExistentEventCategoryException;
import com.teamddd.duckmap.exception.UnableToDeleteEventCategoryInUseException;
import com.teamddd.duckmap.repository.EventCategoryRepository;
import com.teamddd.duckmap.repository.EventInfoCategoryRepository;

@Transactional
@SpringBootTest
class EventCategoryServiceTest {
	@Autowired
	EventCategoryService eventCategoryService;
	@SpyBean
	EventCategoryRepository eventCategoryRepository;
	@MockBean
	EventInfoCategoryRepository eventInfoCategoryRepository;
	@Autowired
	EntityManager em;

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

	@DisplayName("이벤트 카테고리를 Id로 조회한다")
	@Test
	void getEventCategory() throws Exception {
		//given
		EventCategory category1 = createEventCategory("category1");
		eventCategoryRepository.save(category1);

		Long searchCategoryId = category1.getId();
		//when
		EventCategory eventCategory = eventCategoryService.getEventCategory(searchCategoryId);

		//then
		assertThat(eventCategory).extracting("category")
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

	@DisplayName("이벤트 카테고리를 수정한다")
	@Test
	void updateEventCategory() throws Exception {
		//given
		EventCategory category = createEventCategory("category");
		em.persist(category);

		Long updateCategoryId = category.getId();

		em.flush();
		em.clear();

		UpdateEventCategoryServiceReq reqeust = UpdateEventCategoryServiceReq.builder()
			.id(updateCategoryId)
			.category("new_category")
			.build();
		//when
		eventCategoryService.updateEventCategory(reqeust);
		em.flush();
		em.clear();

		//then
		EventCategory findCategory = eventCategoryRepository.findById(updateCategoryId).get();
		assertThat(findCategory).extracting("category")
			.isEqualTo("new_category");
	}

	EventCategory createEventCategory(String category) {
		return EventCategory.builder()
			.category(category)
			.build();
	}

	@DisplayName("이벤트 카테고리를 삭제한다")
	@Nested
	class DeleteEventCategory {
		@DisplayName("사용중인 이벤트가 없는 경우 정상적으로 삭제된다")
		@Test
		void deleteEventCategory1() throws Exception {
			//given
			EventCategory category1 = createEventCategory("category1");
			em.persist(category1);

			em.flush();
			em.clear();

			Mockito.when(eventInfoCategoryRepository.countByEventCategory(Mockito.any())).thenReturn(0L);

			Long deleteCategoryId = category1.getId();
			//when
			eventCategoryService.deleteEventCategory(deleteCategoryId);

			em.flush();
			em.clear();

			//then
			Optional<EventCategory> findCategory = eventCategoryRepository.findById(deleteCategoryId);
			assertThat(findCategory).isEmpty();
		}

		@DisplayName("사용중인 이벤트가 있는 경우 삭제하지 않고 예외 발생")
		@Test
		void deleteEventCategory2() throws Exception {
			//given
			EventCategory category1 = createEventCategory("category1");
			em.persist(category1);

			em.flush();
			em.clear();

			Mockito.when(eventInfoCategoryRepository.countByEventCategory(Mockito.any())).thenReturn(1L);

			Long deleteCategoryId = category1.getId();
			//when //then
			assertThatThrownBy(() -> eventCategoryService.deleteEventCategory(deleteCategoryId))
				.isInstanceOf(UnableToDeleteEventCategoryInUseException.class);
		}
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
