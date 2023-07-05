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
import com.teamddd.duckmap.dto.review.EventReviewServiceReq;
import com.teamddd.duckmap.dto.review.EventReviewsRes;
import com.teamddd.duckmap.dto.review.MyReviewServiceReq;
import com.teamddd.duckmap.dto.review.MyReviewsRes;
import com.teamddd.duckmap.dto.review.ReviewRes;
import com.teamddd.duckmap.dto.review.ReviewSearchServiceReq;
import com.teamddd.duckmap.dto.review.ReviewsRes;
import com.teamddd.duckmap.dto.review.UpdateReviewReq;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.ArtistType;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventArtist;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.entity.Review;
import com.teamddd.duckmap.entity.ReviewImage;
import com.teamddd.duckmap.exception.NonExistentReviewException;
import com.teamddd.duckmap.repository.ReviewRepository;

@Transactional
@SpringBootTest
public class ReviewServiceTest {
	@Autowired
	EntityManager em;
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

	@DisplayName("리뷰를 수정한다")
	@Test
	void updateReview() throws Exception {
		//given
		UpdateReviewReq request = new UpdateReviewReq();
		ReflectionTestUtils.setField(request, "content", "content");
		ReflectionTestUtils.setField(request, "score", 5);
		ReflectionTestUtils.setField(request, "imageFilenames", List.of("filename1", "filename2"));

		Member member = Member.builder()
			.username("member1")
			.build();
		em.persist(member);

		Event event = Event.builder()
			.storeName("store")
			.member(member)
			.build();
		em.persist(event);
		
		Review review = createReview(member, event, "mem1-review1", 4);
		em.persist(review);

		ReviewImage image1 = createReviewImage("image1");
		ReviewImage image2 = createReviewImage("image2");
		ReviewImage image3 = createReviewImage("image3");
		em.persist(image1);
		em.persist(image2);
		em.persist(image3);

		addReviewImage(review, image1);
		addReviewImage(review, image2);
		addReviewImage(review, image3);

		//when
		reviewService.updateReview(review.getId(), request);

		//then
		Optional<Review> findReview = reviewRepository.findById(review.getId());
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

	@DisplayName("회원이 작성한 리뷰를 조회한다")
	@Test
	void getMyReviewsRes() throws Exception {
		//given
		LocalDate now = LocalDate.now();

		Member member1 = Member.builder()
			.username("member1")
			.build();
		em.persist(member1);

		Event event1 = createEvent(member1, "event1", now.minusDays(2), now.plusDays(1));
		Event event2 = createEvent(member1, "event2", now.plusDays(1), now.plusDays(1));
		Event event3 = createEvent(member1, "event3", now.plusDays(1), now.plusDays(1));
		Event event4 = createEvent(member1, "event4", now.plusDays(1), now.plusDays(3));
		em.persist(event1);
		em.persist(event2);
		em.persist(event3);
		em.persist(event4);

		Review review1 = createReview(member1, event1, "mem1-review1", 5);
		Review review2 = createReview(member1, event2, "mem1-review2", 3);
		Review review3 = createReview(member1, event3, "mem1-review3", 4);
		Review review4 = createReview(member1, event4, "mem1-review4", 5);
		em.persist(review1);
		em.persist(review2);
		em.persist(review3);
		em.persist(review4);

		ReviewImage image1 = createReviewImage("image1");
		ReviewImage image2 = createReviewImage("image2");
		ReviewImage image3 = createReviewImage("image3");
		em.persist(image1);
		em.persist(image2);
		em.persist(image3);

		addReviewImage(review1, image1);
		addReviewImage(review1, image2);
		addReviewImage(review2, image3);

		List<Review> reviews = List.of(review1, review2, review3, review4);

		Pageable pageable = PageRequest.of(0, reviews.size());
		MyReviewServiceReq request = MyReviewServiceReq.builder()
			.memberId(member1.getId())
			.pageable(pageable)
			.build();

		//when
		Page<MyReviewsRes> myReviewsResPage = reviewService.getMyReviewsRes(request);

		//then
		assertThat(myReviewsResPage).hasSize(4)
			.extracting("content", "score", "eventStoreName", "reviewImage.fileUrl")
			.containsExactlyInAnyOrder(
				Tuple.tuple("mem1-review1", 5, "event1", "/images/image1"),
				Tuple.tuple("mem1-review2", 3, "event2", "/images/image3"),
				Tuple.tuple("mem1-review3", 4, "event3", "/images/null"),
				Tuple.tuple("mem1-review4", 5, "event4", "/images/null"));
	}

	@DisplayName("조회한 Page<Review>를 Page<ReviewsRes>로 변환하여 반환한다")
	@Test
	void getReviewsResPage() throws Exception {
		//given
		LocalDate now = LocalDate.now();

		Member member1 = Member.builder()
			.username("member1")
			.build();
		em.persist(member1);

		Event event1 = createEvent(member1, "event1", now.minusDays(2), now.plusDays(1));
		Event event2 = createEvent(member1, "event2", now.plusDays(1), now.plusDays(1));
		Event event3 = createEvent(member1, "event3", now.plusDays(1), now.plusDays(1));
		Event event4 = createEvent(member1, "event4", now.plusDays(1), now.plusDays(3));
		em.persist(event1);
		em.persist(event2);
		em.persist(event3);
		em.persist(event4);

		Review review1 = createReview(member1, event1, "mem1-review1", 5);
		Review review2 = createReview(member1, event2, "mem1-review2", 3);
		Review review3 = createReview(member1, event3, "mem1-review3", 4);
		Review review4 = createReview(member1, event4, "mem1-review4", 5);
		em.persist(review1);
		em.persist(review2);
		em.persist(review3);
		em.persist(review4);

		ReviewImage image1 = createReviewImage("image1");
		ReviewImage image2 = createReviewImage("image2");
		ReviewImage image3 = createReviewImage("image3");
		ReviewImage image4 = createReviewImage("image4");
		ReviewImage image5 = createReviewImage("image5");
		em.persist(image1);
		em.persist(image2);
		em.persist(image3);
		em.persist(image4);
		em.persist(image5);

		addReviewImage(review1, image1);
		addReviewImage(review1, image2);
		addReviewImage(review2, image3);
		addReviewImage(review3, image4);
		addReviewImage(review4, image5);

		List<Review> reviews = List.of(review1, review2, review3, review4);

		PageRequest pageRequest = PageRequest.of(0, reviews.size());

		//when
		Page<ReviewsRes> reviewsResPage = reviewService.getReviewsResPage(pageRequest);

		//then
		assertThat(reviewsResPage).hasSize(4)
			.extracting("id", "image.fileUrl")
			.containsExactlyInAnyOrder(
				Tuple.tuple(review1.getId(), "/images/image1"),
				Tuple.tuple(review2.getId(), "/images/image3"),
				Tuple.tuple(review3.getId(), "/images/image4"),
				Tuple.tuple(review4.getId(), "/images/image5"));
	}

	@DisplayName("Artist id, date로 ReviewsRes 목록 조회")
	@Test
	void getReviewsResList() throws Exception {
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
		em.persist(review1);
		em.persist(review2);
		em.persist(review3);
		em.persist(review4);

		ReviewImage image1 = createReviewImage("image1");
		ReviewImage image2 = createReviewImage("image2");
		ReviewImage image3 = createReviewImage("image3");
		ReviewImage image4 = createReviewImage("image4");
		ReviewImage image5 = createReviewImage("image5");
		em.persist(image1);
		em.persist(image2);
		em.persist(image3);
		em.persist(image4);
		em.persist(image5);

		addReviewImage(review1, image1);
		addReviewImage(review1, image2);
		addReviewImage(review2, image3);
		addReviewImage(review3, image4);
		addReviewImage(review4, image5);

		Pageable pageable = PageRequest.of(0, 4);

		ReviewSearchServiceReq request = ReviewSearchServiceReq.builder()
			.date(now)
			.artistId(artist1.getId())
			.onlyInProgress(false)
			.pageable(pageable)
			.build();

		when(artistService.getArtist(any())).thenReturn(artist1);

		//when
		Page<ReviewsRes> reviewsResList = reviewService.getReviewsResList(request);

		//then
		assertThat(reviewsResList).hasSize(2)
			.extracting("id", "image.fileUrl")
			.containsExactlyInAnyOrder(
				Tuple.tuple(review1.getId(), "/images/image1"),
				Tuple.tuple(review3.getId(), "/images/image4"));
	}

	@DisplayName("EventId에 해당하는 EventReviewsRes 목록 조회")
	@Test
	void getEventReviewsResList() throws Exception {
		//given
		LocalDate now = LocalDate.now();

		Member member1 = Member.builder()
			.username("member1")
			.build();
		Member member2 = Member.builder()
			.username("member2")
			.build();
		Member member3 = Member.builder()
			.username("member3")
			.build();
		Member member4 = Member.builder()
			.username("member4")
			.build();
		em.persist(member1);
		em.persist(member2);
		em.persist(member3);
		em.persist(member4);

		Event event1 = createEvent(member1, "event1", now.minusDays(2), now.plusDays(1));
		em.persist(event1);

		Review review1 = createReview(member1, event1, "mem1-review1", 5);
		Review review2 = createReview(member2, event1, "mem2-review2", 3);
		Review review3 = createReview(member3, event1, "mem3-review3", 4);
		Review review4 = createReview(member4, event1, "mem4-review4", 5);
		em.persist(review1);
		em.persist(review2);
		em.persist(review3);
		em.persist(review4);

		ReviewImage image1 = createReviewImage("image1");
		ReviewImage image2 = createReviewImage("image2");
		ReviewImage image3 = createReviewImage("image3");
		ReviewImage image4 = createReviewImage("image4");
		ReviewImage image5 = createReviewImage("image5");
		em.persist(image1);
		em.persist(image2);
		em.persist(image3);
		em.persist(image4);
		em.persist(image5);

		addReviewImage(review1, image1);
		addReviewImage(review1, image2);
		addReviewImage(review2, image3);
		addReviewImage(review3, image4);
		addReviewImage(review4, image5);

		List<Review> reviews = List.of(review1, review2, review3, review4);

		Pageable pageable = PageRequest.of(0, reviews.size());
		EventReviewServiceReq request = EventReviewServiceReq.builder()
			.eventId(event1.getId())
			.pageable(pageable)
			.build();

		//when
		Page<EventReviewsRes> eventReviewsResPage = reviewService.getEventReviewsResList(request);

		//then
		assertThat(eventReviewsResPage).hasSize(4)
			.extracting("content", "score", "reviewImage.fileUrl")
			.containsExactlyInAnyOrder(
				Tuple.tuple("mem1-review1", 5, "/images/image1"),
				Tuple.tuple("mem2-review2", 3, "/images/image3"),
				Tuple.tuple("mem3-review3", 4, "/images/image4"),
				Tuple.tuple("mem4-review4", 5, "/images/image5"));
	}

	@DisplayName("리뷰를 삭제한다")
	@Test
	void deleteReview() throws Exception {
		//given
		LocalDate now = LocalDate.now();

		Member member1 = Member.builder()
			.username("member1")
			.build();
		em.persist(member1);

		Event event1 = createEvent(member1, "event1", now.minusDays(2), now.plusDays(1));
		Event event2 = createEvent(member1, "event2", now.plusDays(1), now.plusDays(1));
		em.persist(event1);
		em.persist(event2);

		Review review1 = createReview(member1, event1, "mem1-review1", 5);
		Review review2 = createReview(member1, event2, "mem1-review2", 3);
		em.persist(review1);
		em.persist(review2);

		ReviewImage image1 = createReviewImage("image1");
		ReviewImage image2 = createReviewImage("image2");
		ReviewImage image3 = createReviewImage("image3");
		em.persist(image1);
		em.persist(image2);
		em.persist(image3);

		addReviewImage(review1, image1);
		addReviewImage(review1, image2);
		addReviewImage(review2, image3);

		//when
		reviewService.deleteReview(review1.getId());

		//then
		Pageable pageable = PageRequest.of(0, 2);
		MyReviewServiceReq request = MyReviewServiceReq.builder()
			.memberId(member1.getId())
			.pageable(pageable)
			.build();

		Page<MyReviewsRes> myReviewsResPage = reviewService.getMyReviewsRes(request);

		assertThat(myReviewsResPage).hasSize(1)
			.extracting("content", "score", "eventStoreName", "reviewImage.fileUrl")
			.containsExactly(
				Tuple.tuple("mem1-review2", 3, "event2", "/images/image3"));
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
		return Review.builder()
			.member(member)
			.event(event)
			.content(content)
			.score(score)
			.build();
	}

	private ReviewImage createReviewImage(String image) {
		return ReviewImage.builder()
			.image(image)
			.build();
	}

	private void addReviewImage(Review review, ReviewImage reviewImage) {
		review.getReviewImages().add(reviewImage);
	}
}
