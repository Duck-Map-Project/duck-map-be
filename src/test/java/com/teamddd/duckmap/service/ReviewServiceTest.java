package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.review.CreateReviewReq;
import com.teamddd.duckmap.dto.review.ReviewRes;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.entity.Review;
import com.teamddd.duckmap.exception.NonExistentReviewException;
import com.teamddd.duckmap.repository.ReviewRepository;

@Transactional
@SpringBootTest
public class ReviewServiceTest {

	@Autowired
	ReviewService reviewService;
	@SpyBean
	ReviewRepository reviewRepository;
	@MockBean
	EventService eventService;

	@DisplayName("리뷰를 생성한다")
	@Test
	void createReview() throws Exception {
		//given
		CreateReviewReq request = new CreateReviewReq();
		ReflectionTestUtils.setField(request, "content", "content");
		ReflectionTestUtils.setField(request, "score", 5);
		ReflectionTestUtils.setField(request, "imageFilenames", List.of("filename"));
		ReflectionTestUtils.setField(request, "eventId", 1L);

		Member member = Member.builder()
			.username("member1")
			.build();

		Event event = Event.builder()
			.storeName("store")
			.member(member)
			.build();

		when(eventService.getEvent(any())).thenReturn(event);
		//when
		Long reviewId = reviewService.createReview(request, member);

		//then
		assertThat(reviewId).isNotNull();

		Optional<Review> findReview = reviewRepository.findById(reviewId);
		assertThat(findReview).isNotEmpty();
		assertThat(findReview.get())
			.extracting("content", "score", "event.storeName", "member.username")
			.containsOnly("content", 5, "store", "member1");

	}

	@DisplayName("리뷰를 조회한다")
	@Nested
	class GetReview {
		@DisplayName("유효한 값으로 리뷰를 조회한다")
		@Test
		void getReview1() throws Exception {
			//given
			CreateReviewReq request = new CreateReviewReq();
			ReflectionTestUtils.setField(request, "content", "content");
			ReflectionTestUtils.setField(request, "score", 5);
			ReflectionTestUtils.setField(request, "imageFilenames", List.of("filename"));
			ReflectionTestUtils.setField(request, "eventId", 1L);

			Member member = Member.builder()
				.username("member1")
				.build();

			Event event = Event.builder()
				.storeName("store")
				.member(member)
				.build();

			when(eventService.getEvent(any())).thenReturn(event);
			Long reviewId = reviewService.createReview(request, member);

			//when
			ReviewRes reviewRes = reviewService.getReviewById(reviewId);

			//then
			assertThat(reviewRes).extracting("content", "score")
				.contains("content", 5);
		}

		@DisplayName("잘못된 값으로 리뷰를 조회할 수 없다")
		@Test
		void getReview2() throws Exception {
			//given
			Long reviewId = 1L;

			//when //then
			assertThatThrownBy(() -> reviewService.getReviewById(reviewId))
				.isInstanceOf(NonExistentReviewException.class)
				.hasMessage("잘못된 리뷰 정보입니다");
		}
	}

}
