package com.teamddd.duckmap.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.review.CreateReviewReq;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.entity.Review;
import com.teamddd.duckmap.repository.ReviewRepository;

@Transactional
@SpringBootTest
public class ReviewServiceTest {

	@Autowired
	ReviewService reviewService;
	@SpyBean
	ReviewRepository reviewRepository;

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

		//when
		Long reviewId = reviewService.createReview(request, member);

		//then
		assertThat(reviewId).isNotNull();

		Optional<Review> findReview = reviewRepository.findById(reviewId);
		assertThat(findReview).isNotEmpty();
		assertThat(findReview.get())
			.extracting("content", "score", "eventId", "member.username")
			.containsOnly("content", 5, 1L, "member1");

	}

}
