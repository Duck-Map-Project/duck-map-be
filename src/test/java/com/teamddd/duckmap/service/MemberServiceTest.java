package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.user.CreateMemberReq;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventLike;
import com.teamddd.duckmap.entity.LastSearchArtist;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.repository.EventLikeRepository;
import com.teamddd.duckmap.repository.LastSearchArtistRepository;
import com.teamddd.duckmap.repository.MemberRepository;

@SpringBootTest
@Transactional
public class MemberServiceTest {
	@Autowired
	EntityManager em;
	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	LastSearchArtistRepository lastSearchArtistRepository;
	@Autowired
	EventLikeRepository eventLikeRepository;

	@DisplayName("생성된 회원 확인")
	@Test
	void createMember() throws Exception {
		//given
		CreateMemberReq request = new CreateMemberReq();
		ReflectionTestUtils.setField(request, "username", "user1");
		ReflectionTestUtils.setField(request, "email", "string@string.com");
		ReflectionTestUtils.setField(request, "password", "@Aaaa1234523");
		//when
		Long memberId = memberService.join(request);
		//then
		assertThat(memberId).isNotNull();
		Optional<Member> findMember = memberRepository.findById(memberId);
		assertThat(findMember).isNotEmpty();
		assertThat(findMember.get())
			.extracting("username")
			.isEqualTo("user1");
	}

	@DisplayName("회원 탈퇴시 이벤트좋아요, 마지막검색 아티스트도 삭제")
	@Test
	void deleteMember() throws Exception {
		//given
		Member member1 = Member.builder().build();
		Member member2 = Member.builder().build();
		em.persist(member1);
		em.persist(member2);

		Event event1 = createEvent(member1, "event1");
		Event event2 = createEvent(member1, "event2");
		Event event3 = createEvent(member1, "event3");
		em.persist(event1);
		em.persist(event2);
		em.persist(event3);

		EventLike eventLike1 = createEventLike(member1, event1);
		EventLike eventLike2 = createEventLike(member1, event2);
		EventLike eventLike3 = createEventLike(member2, event1);
		EventLike eventLike4 = createEventLike(member2, event3);
		em.persist(eventLike1);
		em.persist(eventLike2);
		em.persist(eventLike3);
		em.persist(eventLike4);

		Artist artist1 = createArtist("artist1");
		Artist artist2 = createArtist("artist2");
		Artist artist3 = createArtist("artist3");
		em.persist(artist1);
		em.persist(artist2);
		em.persist(artist3);

		LastSearchArtist lastSearchArtist1 = createLastSearchArtist(member1, artist1);
		LastSearchArtist lastSearchArtist2 = createLastSearchArtist(member1, artist2);
		LastSearchArtist lastSearchArtist3 = createLastSearchArtist(member2, artist3);
		em.persist(lastSearchArtist1);
		em.persist(lastSearchArtist2);
		em.persist(lastSearchArtist3);

		//when
		memberService.deleteMember(member1.getId());

		//then
		Optional<Member> findMember = memberRepository.findById(member1.getId());
		assertThat(findMember).isEmpty();
		List<EventLike> findEventLike = eventLikeRepository.findAll();
		assertThat(findEventLike).hasSize(2);
		List<LastSearchArtist> findLateSearchArtist = lastSearchArtistRepository.findAll();
		assertThat(findLateSearchArtist).hasSize(1);
	}

	private Event createEvent(Member member, String storeName) {
		return Event.builder().member(member).storeName(storeName).build();
	}

	private EventLike createEventLike(Member member, Event event) {
		return EventLike.builder().member(member).event(event).build();
	}

	Artist createArtist(String name) {
		return Artist.builder()
			.group(null)
			.artistType(null)
			.image("image")
			.name(name)
			.build();
	}

	LastSearchArtist createLastSearchArtist(Member member, Artist artist) {
		return LastSearchArtist.builder()
			.member(member)
			.artist(artist)
			.build();
	}
}
