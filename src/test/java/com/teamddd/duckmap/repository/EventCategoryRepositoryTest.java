package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.EventCategory;

@Transactional
@SpringBootTest
class EventCategoryRepositoryTest {
	@Autowired
	EventCategoryRepository eventCategoryRepository;
	@Autowired
	EntityManager em;

	@DisplayName("pk 목록으로 EventCategory 목록 조회")
	@Test
	void findByIdIn() throws Exception {
		//given
		EventCategory category1 = createEventCategory("category1");
		EventCategory category2 = createEventCategory("category2");
		EventCategory category3 = createEventCategory("category3");
		EventCategory category4 = createEventCategory("category4");
		em.persist(category1);
		em.persist(category2);
		em.persist(category3);
		em.persist(category4);

		List<Long> inIds = List.of(category1.getId(), category4.getId());

		//when
		List<EventCategory> findCategories = eventCategoryRepository.findByIdIn(inIds);

		//then
		assertThat(findCategories).hasSize(2)
			.extracting("category")
			.containsExactlyInAnyOrder("category1", "category4");
	}

	EventCategory createEventCategory(String category) {
		return EventCategory.builder()
			.category(category)
			.build();
	}

}
