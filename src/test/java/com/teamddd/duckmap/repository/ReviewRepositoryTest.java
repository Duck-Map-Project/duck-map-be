package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.review.ReviewEventDto;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventArtist;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.entity.Review;

@Transactional
@SpringBootTest
class ReviewRepositoryTest {
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	EntityManager em;

	@DisplayName("Event로 review 목록 조회")
	@Test
	void findByEvent() throws Exception {
		//given
		Member member1 = Member.builder().username("user1").build();
		Member member2 = Member.builder().username("user2").build();
		Member member3 = Member.builder().username("user3").build();
		em.persist(member1);
		em.persist(member2);
		em.persist(member3);

		Event event = createEvent("event1");
		Event event2 = createEvent("event2");
		Event event3 = createEvent("event3");
		em.persist(event);
		em.persist(event2);
		em.persist(event3);

		Review review = createReview(member1, event, "user1 review1", 5);
		Review review2 = createReview(member1, event2, "user1 review2", 5);
		Review review3 = createReview(member1, event3, "user1 review3", 5);
		Review review4 = createReview(member2, event, "user2 review1", 5);
		Review review5 = createReview(member2, event2, "user2 review2", 5);
		Review review6 = createReview(member3, event, "user3 review1", 5);
		em.persist(review);
		em.persist(review2);
		em.persist(review3);
		em.persist(review4);
		em.persist(review5);
		em.persist(review6);

		PageRequest pageRequest = PageRequest.of(0, 2);

		//when
		Page<Review> reviews = reviewRepository.findByEvent(event, pageRequest);
		Page<Review> reviews2 = reviewRepository.findByEvent(event2, pageRequest);

		//then
		assertThat(reviews).hasSize(2)
			.extracting("id", "content", "event.storeName")
			.containsExactlyInAnyOrder(
				Tuple.tuple(review.getId(), review.getContent(), event.getStoreName()),
				Tuple.tuple(review4.getId(), review4.getContent(), event.getStoreName()));

		assertThat(reviews.getTotalElements()).isEqualTo(3);
		assertThat(reviews.getTotalPages()).isEqualTo(2);

		assertThat(reviews2).hasSize(2)
			.extracting("id", "content", "event.storeName")
			.containsExactlyInAnyOrder(
				Tuple.tuple(review2.getId(), review2.getContent(), event2.getStoreName()),
				Tuple.tuple(review5.getId(), review5.getContent(), event2.getStoreName()));

		assertThat(reviews2.getTotalElements()).isEqualTo(2);
		assertThat(reviews2.getTotalPages()).isEqualTo(1);
	}

	@DisplayName("MemberId로 my review 목록 조회")
	@Test
	void findByUser() throws Exception {
		//given
		Member member1 = Member.builder().username("user1").build();
		Member member2 = Member.builder().username("user2").build();
		Member member3 = Member.builder().username("user2").build();
		em.persist(member1);
		em.persist(member2);
		em.persist(member3);

		Event event = createEvent("event1");
		Event event2 = createEvent("event2");
		Event event3 = createEvent("event3");
		em.persist(event);
		em.persist(event2);
		em.persist(event3);

		Review review = createReview(member1, event, "user1 review1", 5);
		Review review2 = createReview(member1, event2, "user1 review2", 5);
		Review review3 = createReview(member1, event3, "user1 review3", 5);
		Review review4 = createReview(member2, event, "user2 review1", 5);
		Review review5 = createReview(member2, event2, "user2 review2", 5);
		Review review6 = createReview(member3, event3, "user3 review1", 5);
		em.persist(review);
		em.persist(review2);
		em.persist(review3);
		em.persist(review4);
		em.persist(review5);
		em.persist(review6);

		PageRequest pageRequest = PageRequest.of(0, 2);

		//when
		Page<Review> reviews = reviewRepository.findByMember(member1, pageRequest);
		Page<Review> reviews2 = reviewRepository.findByMember(member2, pageRequest);
		//then
		assertThat(reviews).hasSize(2)
			.extracting("id", "content", "event.storeName")
			.containsExactlyInAnyOrder(
				Tuple.tuple(review.getId(), review.getContent(), event.getStoreName()),
				Tuple.tuple(review2.getId(), review2.getContent(), event2.getStoreName()));

		assertThat(reviews.getTotalElements()).isEqualTo(3);
		assertThat(reviews.getTotalPages()).isEqualTo(2);

		assertThat(reviews2).hasSize(2)
			.extracting("id", "content", "event.storeName")
			.containsExactlyInAnyOrder(
				Tuple.tuple(review4.getId(), review4.getContent(), event.getStoreName()),
				Tuple.tuple(review5.getId(), review5.getContent(), event2.getStoreName()));

		assertThat(reviews2.getTotalElements()).isEqualTo(2);
		assertThat(reviews2.getTotalPages()).isEqualTo(1);
	}

	@DisplayName("MemberId로 my review 목록 조회, Dto 반환")
	@Test
	void findWithEventByMemberId() throws Exception {
		//given
		Member member1 = Member.builder().username("user1").build();
		Member member2 = Member.builder().username("user2").build();
		Member member3 = Member.builder().username("user2").build();
		em.persist(member1);
		em.persist(member2);
		em.persist(member3);

		Event event = createEvent("event1");
		Event event2 = createEvent("event2");
		Event event3 = createEvent("event3");
		em.persist(event);
		em.persist(event2);
		em.persist(event3);

		Review review = createReview(member1, event, "user1 review1", 5);
		Review review2 = createReview(member1, event2, "user1 review2", 5);
		Review review3 = createReview(member1, event3, "user1 review3", 5);
		Review review4 = createReview(member2, event, "user2 review1", 5);
		Review review5 = createReview(member2, event2, "user2 review2", 5);
		Review review6 = createReview(member3, event3, "user3 review1", 5);
		em.persist(review);
		em.persist(review2);
		em.persist(review3);
		em.persist(review4);
		em.persist(review5);
		em.persist(review6);

		PageRequest pageRequest = PageRequest.of(0, 2);

		//when
		Page<ReviewEventDto> reviews = reviewRepository.findWithEventByMemberId(member1.getId(), pageRequest);
		Page<ReviewEventDto> reviews2 = reviewRepository.findWithEventByMemberId(member2.getId(), pageRequest);
		//then
		assertThat(reviews).hasSize(2)
			.extracting("review.id", "review.content", "eventStoreName")
			.containsExactlyInAnyOrder(
				Tuple.tuple(review.getId(), review.getContent(), event.getStoreName()),
				Tuple.tuple(review2.getId(), review2.getContent(), event2.getStoreName()));

		assertThat(reviews.getTotalElements()).isEqualTo(3);
		assertThat(reviews.getTotalPages()).isEqualTo(2);

		assertThat(reviews2).hasSize(2)
			.extracting("review.id", "review.content", "eventStoreName")
			.containsExactlyInAnyOrder(
				Tuple.tuple(review4.getId(), review4.getContent(), event.getStoreName()),
				Tuple.tuple(review5.getId(), review5.getContent(), event2.getStoreName()));

		assertThat(reviews2.getTotalElements()).isEqualTo(2);
		assertThat(reviews2.getTotalPages()).isEqualTo(1);
	}

	@DisplayName("Event fk로 조회한 Review 목록의 score 평균 계산")
	@Test
	void avgScoreByEvent() throws Exception {
		//given
		Event event = createEvent("event");
		em.persist(event);

		// score sum = 25, avg = 4.1666...
		Review review1 = createReview(null, event, "", 3);
		Review review2 = createReview(null, event, "", 4);
		Review review3 = createReview(null, event, "", 3);
		Review review4 = createReview(null, event, "", 5);
		Review review5 = createReview(null, event, "", 5);
		Review review6 = createReview(null, event, "", 5);
		em.persist(review1);
		em.persist(review2);
		em.persist(review3);
		em.persist(review4);
		em.persist(review5);
		em.persist(review6);

		Long eventId = event.getId();

		//when
		Optional<Double> avgScore = reviewRepository.avgScoreByEvent(eventId);

		//then
		assertThat(avgScore).isNotEmpty();
		assertThat(avgScore.get()).isEqualTo(4.2);
	}

	private Event createEvent(String storeName) {
		return Event.builder().storeName(storeName).build();
	}

	private Event createEventComplex(String storeName, LocalDate fromDate, LocalDate toDate) {
		return Event.builder()
			.storeName(storeName)
			.fromDate(fromDate)
			.toDate(toDate)
			.build();
	}

	private Review createReview(Member member, Event event, String content, int score) {
		return Review.builder().member(member).event(event).content(content).score(score).build();
	}

	private EventArtist createEventArtist(Event event, Artist artist) {
		return EventArtist.builder()
			.event(event)
			.artist(artist)
			.build();
	}

	@Nested
	@DisplayName("artistId와 fromDate <= date <= toDate로 Review 조회")
	class FindByArtistAndDate {
		@DisplayName("artistId,date가 전부 존재")
		@Test
		void findByArtistAndDate1() throws Exception {
			//given
			LocalDate date = LocalDate.now();

			Member member1 = Member.builder().username("user1").build();
			Member member2 = Member.builder().username("user2").build();
			Member member3 = Member.builder().username("user3").build();
			em.persist(member1);
			em.persist(member2);
			em.persist(member3);

			Event event1 = createEventComplex("event1", date.minusDays(3), date.plusDays(2));
			Event event2 = createEventComplex("event2", date.minusDays(7), date.minusDays(3));
			Event event3 = createEventComplex("event3", date, date.plusDays(4));
			Event event4 = createEventComplex("event4", date, date.minusDays(1).plusDays(2));
			em.persist(event1);
			em.persist(event2);
			em.persist(event3);
			em.persist(event4);

			Artist artist1 = Artist.builder().build();
			Artist artist2 = Artist.builder().build();
			em.persist(artist1);
			em.persist(artist2);

			EventArtist eventArtist1 = createEventArtist(event1, artist1);
			EventArtist eventArtist2 = createEventArtist(event2, artist1);
			EventArtist eventArtist3 = createEventArtist(event3, artist2);
			EventArtist eventArtist4 = createEventArtist(event4, artist2);
			em.persist(eventArtist1);
			em.persist(eventArtist2);
			em.persist(eventArtist3);
			em.persist(eventArtist4);

			Review review1 = createReview(member1, event1, "user1 review1", 5);
			Review review2 = createReview(member1, event2, "user1 review2", 5);
			Review review3 = createReview(member1, event3, "user1 review3", 5);
			Review review4 = createReview(member2, event3, "user2 review1", 5);
			Review review5 = createReview(member2, event4, "user2 review2", 5);
			Review review6 = createReview(member3, event1, "user3 review1", 5);
			em.persist(review1);
			em.persist(review2);
			em.persist(review3);
			em.persist(review4);
			em.persist(review5);
			em.persist(review6);

			PageRequest pageRequest = PageRequest.of(0, 2);

			//when
			Page<Review> reviews1 = reviewRepository.findByArtistAndDate(artist2.getId(), date, pageRequest);

			//then
			assertThat(reviews1).hasSize(2)
				.extracting("id", "content", "event.storeName")
				.contains(
					Tuple.tuple(review3.getId(), review3.getContent(), event3.getStoreName()),
					Tuple.tuple(review4.getId(), review4.getContent(), event3.getStoreName()));
			assertThat(reviews1.getTotalElements()).isEqualTo(3);
			assertThat(reviews1.getTotalPages()).isEqualTo(2);

		}

		@DisplayName("date가 null")
		@Test
		void findByArtistAndDate2() throws Exception {
			//given
			LocalDate date = LocalDate.now();

			Member member1 = Member.builder().username("user1").build();
			Member member2 = Member.builder().username("user2").build();
			Member member3 = Member.builder().username("user3").build();
			em.persist(member1);
			em.persist(member2);
			em.persist(member3);

			Event event1 = createEventComplex("event1", date.minusDays(3), date.plusDays(2));
			Event event2 = createEventComplex("event2", date.minusDays(7), date.minusDays(3));
			Event event3 = createEventComplex("event3", date, date.plusDays(4));
			Event event4 = createEventComplex("event4", date.minusDays(1), date.plusDays(2));
			em.persist(event1);
			em.persist(event2);
			em.persist(event3);
			em.persist(event4);

			Artist artist1 = Artist.builder().build();
			Artist artist2 = Artist.builder().build();
			em.persist(artist1);
			em.persist(artist2);

			EventArtist eventArtist1 = createEventArtist(event1, artist1);
			EventArtist eventArtist2 = createEventArtist(event2, artist1);
			EventArtist eventArtist3 = createEventArtist(event3, artist2);
			EventArtist eventArtist4 = createEventArtist(event4, artist2);
			em.persist(eventArtist1);
			em.persist(eventArtist2);
			em.persist(eventArtist3);
			em.persist(eventArtist4);

			Review review1 = createReview(member1, event1, "user1 review1", 5);
			Review review2 = createReview(member1, event2, "user1 review2", 5);
			Review review3 = createReview(member1, event3, "user1 review3", 5);
			Review review4 = createReview(member2, event3, "user2 review1", 5);
			Review review5 = createReview(member2, event4, "user2 review2", 5);
			Review review6 = createReview(member3, event1, "user3 review1", 5);
			em.persist(review1);
			em.persist(review2);
			em.persist(review3);
			em.persist(review4);
			em.persist(review5);
			em.persist(review6);

			PageRequest pageRequest = PageRequest.of(0, 2);

			//when
			Page<Review> reviews2 = reviewRepository.findByArtistAndDate(artist1.getId(), null, pageRequest);

			//then
			assertThat(reviews2).hasSize(2)
				.extracting("id", "content", "event.storeName")
				.contains(
					Tuple.tuple(review1.getId(), review1.getContent(), event1.getStoreName()),
					Tuple.tuple(review2.getId(), review2.getContent(), event2.getStoreName()));
			assertThat(reviews2.getTotalElements()).isEqualTo(3);
			assertThat(reviews2.getTotalPages()).isEqualTo(2);
		}

		@DisplayName("artistId가 null")
		@Test
		void findByArtistAndDate3() throws Exception {
			//given
			LocalDate date = LocalDate.now();

			Member member1 = Member.builder().username("user1").build();
			Member member2 = Member.builder().username("user2").build();
			Member member3 = Member.builder().username("user3").build();
			em.persist(member1);
			em.persist(member2);
			em.persist(member3);

			Event event1 = createEventComplex("event1", date.minusDays(5), date.minusDays(2));
			Event event2 = createEventComplex("event2", date.minusDays(7), date.minusDays(3));
			Event event3 = createEventComplex("event3", date, date.plusDays(4));
			Event event4 = createEventComplex("event4", date, date.minusDays(1).plusDays(2));
			em.persist(event1);
			em.persist(event2);
			em.persist(event3);
			em.persist(event4);

			Artist artist1 = Artist.builder().build();
			Artist artist2 = Artist.builder().build();
			em.persist(artist1);
			em.persist(artist2);

			EventArtist eventArtist1 = createEventArtist(event1, artist1);
			EventArtist eventArtist2 = createEventArtist(event2, artist1);
			EventArtist eventArtist3 = createEventArtist(event3, artist2);
			EventArtist eventArtist4 = createEventArtist(event4, artist2);
			em.persist(eventArtist1);
			em.persist(eventArtist2);
			em.persist(eventArtist3);
			em.persist(eventArtist4);

			Review review1 = createReview(member1, event1, "user1 review1", 5);
			Review review2 = createReview(member1, event2, "user1 review2", 5);
			Review review3 = createReview(member1, event3, "user1 review3", 5);
			Review review4 = createReview(member2, event3, "user2 review1", 5);
			Review review5 = createReview(member2, event4, "user2 review2", 5);
			Review review6 = createReview(member3, event1, "user3 review1", 5);
			em.persist(review1);
			em.persist(review2);
			em.persist(review3);
			em.persist(review4);
			em.persist(review5);
			em.persist(review6);

			PageRequest pageRequest = PageRequest.of(0, 2);

			//when
			Page<Review> reviews3 = reviewRepository.findByArtistAndDate(null, date, pageRequest);

			//then
			assertThat(reviews3).hasSize(2)
				.extracting("id", "content", "event.storeName")
				.containsExactlyInAnyOrder(
					Tuple.tuple(review3.getId(), review3.getContent(), event3.getStoreName()),
					Tuple.tuple(review4.getId(), review4.getContent(), event3.getStoreName()));
			assertThat(reviews3.getTotalElements()).isEqualTo(3);
			assertThat(reviews3.getTotalPages()).isEqualTo(2);
		}
	}
}
