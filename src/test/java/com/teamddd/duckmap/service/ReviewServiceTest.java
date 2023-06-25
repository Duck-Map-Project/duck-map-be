package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.review.CreateReviewReq;
import com.teamddd.duckmap.dto.review.ReviewRes;
import com.teamddd.duckmap.dto.review.ReviewSearchServiceReq;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.ArtistType;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventArtist;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.entity.Review;
import com.teamddd.duckmap.exception.NonExistentReviewException;
import com.teamddd.duckmap.repository.EventRepository;
import com.teamddd.duckmap.repository.ReviewRepository;

@Transactional
@SpringBootTest
public class ReviewServiceTest {
	@Autowired
	EntityManager em;
	@SpyBean
	EventRepository eventRepository;
	@MockBean
	ArtistService artistService;
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
		ReflectionTestUtils.setField(request, "imageFilenames", List.of("filename1", "filename2"));
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
		assertThat(findReview.get().getReviewImages()).hasSize(2)
			.extracting("image").containsExactlyInAnyOrder("filename1", "filename2");

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
			ReviewRes reviewRes = reviewService.getReviewRes(reviewId);

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
			assertThatThrownBy(() -> reviewService.getReviewRes(reviewId))
				.isInstanceOf(NonExistentReviewException.class)
				.hasMessage("잘못된 리뷰 정보입니다");
		}
	}

	@DisplayName("Artist id, date로 ReviewRes 목록 조회")
	@Test
	void getReviewResList() throws Exception {
		//given
		LocalDate now = LocalDate.now();

		// save Member
		Member member1 = Member.builder()
			.username("member1")
			.build();
		Member member2 = Member.builder()
			.username("member2")
			.build();
		em.persist(member1);
		em.persist(member2);

		// save Artist
		ArtistType artistType = createArtistType();
		em.persist(artistType);
		Artist artist1 = createArtist("artist1", artistType);
		Artist artist2 = createArtist("artist2", artistType);
		em.persist(artist1);
		em.persist(artist2);

		//save Event
		Event event1 = createEvent(member1, "event1", now.minusDays(2), now.plusDays(1));
		Event event2 = createEvent(member1, "event2", now.plusDays(1), now.plusDays(1));
		Event event3 = createEvent(member1, "event3", now.plusDays(1), now.plusDays(1));
		Event event4 = createEvent(member1, "event4", now.plusDays(1), now.plusDays(3));
		em.persist(event1);
		em.persist(event2);
		em.persist(event3);
		em.persist(event4);

		// Artist, EventCategory, EventImage -> event
		EventArtist eventArtist1 = createEventArtist(event1, artist1);
		EventArtist eventArtist2 = createEventArtist(event2, artist2);
		EventArtist eventArtist3 = createEventArtist(event3, artist1);
		EventArtist eventArtist4 = createEventArtist(event4, artist2);
		em.persist(eventArtist1);
		em.persist(eventArtist2);
		em.persist(eventArtist3);
		em.persist(eventArtist4);

		// save Review
		Review review1 = createReview(member1, event1, "mem1-review1", 5);
		Review review2 = createReview(member1, event2, "mem1-review2", 3);
		Review review3 = createReview(member1, event3, "mem1-review3", 4);
		Review review4 = createReview(member1, event4, "mem1-review4", 5);
		Review review5 = createReview(member2, event1, "mem2-review1", 5);
		Review review6 = createReview(member2, event2, "mem2-review2", 4);
		Review review7 = createReview(member2, event3, "mem2-review3", 5);
		Review review8 = createReview(member2, event4, "mem2-review4", 5);
		em.persist(review1);
		em.persist(review2);
		em.persist(review3);
		em.persist(review4);
		em.persist(review5);
		em.persist(review6);
		em.persist(review7);
		em.persist(review8);

		em.flush();
		em.clear();

		Artist searchArtist = artist1;

		Pageable pageable = PageRequest.of(0, 4);

		ReviewSearchServiceReq request = ReviewSearchServiceReq.builder()
			.date(now)
			.artistId(searchArtist.getId())
			.onlyInProgress(false)
			.pageable(pageable)
			.build();

		when(artistService.getArtist(any())).thenReturn(searchArtist);

		//when
		Page<ReviewRes> reviewResList = reviewService.getReviewResList(request);

		//then
		assertThat(reviewResList).hasSize(4)
			.extracting("username", "content", "score")
			.containsExactly(
				Tuple.tuple("member1", "mem1-review1", 5),
				Tuple.tuple("member1", "mem1-review3", 4),
				Tuple.tuple("member2", "mem2-review1", 5),
				Tuple.tuple("member2", "mem2-review3", 5));
	}

	ArtistType createArtistType() {
		return ArtistType.builder().type("artist_type").build();
	}

	Artist createArtist(String name, ArtistType type) {
		return Artist.builder().name(name).artistType(type).build();
	}

	EventArtist createEventArtist(Event event, Artist artist) {
		return EventArtist.builder().event(event).artist(artist).build();
	}

	Event createEvent(Member member, String storeName, LocalDate fromDate, LocalDate toDate) {
		return Event.builder()
			.member(member)
			.storeName(storeName)
			.fromDate(fromDate)
			.toDate(toDate)
			.eventImages(List.of())
			.build();
	}

	private Review createReview(Member member, Event event, String content, int score) {
		return Review.builder().member(member).event(event).content(content).score(score).build();
	}
}
