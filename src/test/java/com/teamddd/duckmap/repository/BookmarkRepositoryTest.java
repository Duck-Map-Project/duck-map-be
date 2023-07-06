package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventBookmark;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.Member;

@SpringBootTest
@Transactional
public class BookmarkRepositoryTest {
	@Autowired
	EntityManager em;
	@Autowired
	BookmarkRepository bookmarkRepository;

	@DisplayName("BookmarkFolderId로 북마크 삭제")
	@Test
	public void deleteByBookmarkFolderId() throws Exception {
		//given
		Member member = Member.builder().username("member1").build();
		em.persist(member);

		Event event = createEvent(member, "event1");
		Event event2 = createEvent(member, "event2");
		em.persist(event);
		em.persist(event2);

		EventBookmarkFolder eventBookmarkFolder = createEventBookmarkFolder(member, "folder1");
		EventBookmarkFolder eventBookmarkFolder2 = createEventBookmarkFolder(member, "folder2");
		em.persist(eventBookmarkFolder);
		em.persist(eventBookmarkFolder2);

		EventBookmark eventBookmark = createEventBookmark(member, event, eventBookmarkFolder);
		EventBookmark eventBookmark2 = createEventBookmark(member, event2, eventBookmarkFolder2);
		EventBookmark eventBookmark3 = createEventBookmark(member, event2, eventBookmarkFolder);
		em.persist(eventBookmark);
		em.persist(eventBookmark2);
		em.persist(eventBookmark3);

		//when
		int deleteCount = bookmarkRepository.deleteByBookmarkFolderId(eventBookmarkFolder.getId());

		//then
		assertThat(deleteCount).isEqualTo(2);

		List<EventBookmark> findBookmark = bookmarkRepository.findAll();
		assertThat(findBookmark).hasSize(1);
	}

	@DisplayName("eventId와 memberId로 북마크 조회")
	@Test
	public void findByEventAndMember() throws Exception {
		//given
		Member member = Member.builder().username("member1").build();
		em.persist(member);

		Event event = createEvent(member, "event1");
		Event event2 = createEvent(member, "event2");
		em.persist(event);
		em.persist(event2);

		EventBookmarkFolder eventBookmarkFolder = createEventBookmarkFolder(member, "folder1");
		EventBookmarkFolder eventBookmarkFolder2 = createEventBookmarkFolder(member, "folder2");
		em.persist(eventBookmarkFolder);
		em.persist(eventBookmarkFolder2);

		EventBookmark eventBookmark = createEventBookmark(member, event, eventBookmarkFolder);
		EventBookmark eventBookmark2 = createEventBookmark(member, event2, eventBookmarkFolder2);

		em.persist(eventBookmark);
		em.persist(eventBookmark2);

		//when
		Optional<EventBookmark> findBookmark = bookmarkRepository.findByEventIdAndMemberId(event.getId(),
			member.getId());

		//then
		assertThat(findBookmark).isNotEmpty();
		assertThat(findBookmark.get())
			.extracting("event.storeName", "member.username")
			.containsOnly("event1", "member1");
	}

	private Event createEvent(Member member, String storeName) {
		return Event.builder().member(member).storeName(storeName).build();
	}

	private EventBookmark createEventBookmark(Member member, Event event, EventBookmarkFolder eventBookmarkFolder) {
		return EventBookmark.builder().member(member).event(event).eventBookmarkFolder(eventBookmarkFolder).build();
	}

	private EventBookmarkFolder createEventBookmarkFolder(Member member, String name) {
		return EventBookmarkFolder.builder().member(member).name(name).build();
	}
}
