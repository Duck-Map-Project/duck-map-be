package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.Review;
import com.teamddd.duckmap.entity.User;

@Transactional
@SpringBootTest
public class ReviewRepositoryTest {
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	EntityManager em;

	@DisplayName("UserId로 my review 목록 조회")
	@Test
	void findByUserId() throws Exception {
		//given
		User user1 = User.builder().username("user1").build();
		em.persist(user1);

		Event event = createEvent("event1");
		Event event2 = createEvent("event2");
		Event event3 = createEvent("event3");
		Event event4 = createEvent("event4");
		em.persist(event);
		em.persist(event2);
		em.persist(event3);
		em.persist(event4);

		Review review = createReview(user1, event, "review1", 5);
		Review review2 = createReview(user1, event2, "review2", 5);
		Review review3 = createReview(user1, event3, "review3", 5);
		em.persist(review);
		em.persist(review2);
		em.persist(review3);

		//when
		List<Review> reviews = reviewRepository.findByUserId(user1.getId());

		//then
		assertThat(reviews).hasSize(3)
			.extracting("id", "content", "event.storeName")
			.containsExactlyInAnyOrder(
				Tuple.tuple(review.getId(), review.getContent(), event.getStoreName()),
				Tuple.tuple(review2.getId(), review2.getContent(), event2.getStoreName()),
				Tuple.tuple(review3.getId(), review3.getContent(), event3.getStoreName()));
	}

	private Event createEvent(String storeName) {
		return Event.builder().storeName(storeName).build();
	}

	private Review createReview(User user, Event event, String content, int score) {
		return Review.builder().user(user).event(event).content(content).score(score).build();
	}
}
