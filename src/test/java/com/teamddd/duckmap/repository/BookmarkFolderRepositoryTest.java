package com.teamddd.duckmap.repository;

import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderEventDto;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventBookmark;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.User;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
public class BookmarkFolderRepositoryTest {
	@Autowired
	BookmarkFolderRepository bookmarkFolderRepository;
	@Autowired
	EntityManager em;

	@DisplayName("bookmarkfolderId로 이벤트 목록 조회")
	@Test
	public void findBookmarkedEvents() throws Exception {
		//given
		User user = User.builder().build();
		em.persist(user);

		Event event = createEvent(user, "event1");
		Event event2 = createEvent(user, "event2");
		Event event3 = createEvent(user, "event3");
		Event event4 = createEvent(user, "event4");
		em.persist(event);
		em.persist(event2);
		em.persist(event3);
		em.persist(event4);

		EventBookmarkFolder eventBookmarkFolder = createEventBookmarkFolder("folder1");
		EventBookmarkFolder eventBookmarkFolder2 = createEventBookmarkFolder("folder2");
		em.persist(eventBookmarkFolder);
		em.persist(eventBookmarkFolder2);

		EventBookmark eventBookmark = createEventBookmark(user, event, eventBookmarkFolder);
		EventBookmark eventBookmark2 = createEventBookmark(user, event2, eventBookmarkFolder);
		EventBookmark eventBookmark3 = createEventBookmark(user, event, eventBookmarkFolder2);
		EventBookmark eventBookmark4 = createEventBookmark(user, event3, eventBookmarkFolder2);
		EventBookmark eventBookmark5 = createEventBookmark(user, event4, eventBookmarkFolder2);

		em.persist(eventBookmark);
		em.persist(eventBookmark2);
		em.persist(eventBookmark3);
		em.persist(eventBookmark4);
		em.persist(eventBookmark5);

		PageRequest pageRequest = PageRequest.of(0, 2);
		PageRequest pageRequest2 = PageRequest.of(0, 2);
		//when
		Page<BookmarkFolderEventDto> bookmarkedEvents = bookmarkFolderRepository
				.findBookmarkedEvents(eventBookmarkFolder.getId(), pageRequest);
		Page<BookmarkFolderEventDto> bookmarkedEvents2 = bookmarkFolderRepository
				.findBookmarkedEvents(eventBookmarkFolder2.getId(), pageRequest2);
		//then
		assertThat(bookmarkedEvents).hasSize(2)
				.extracting("event.id", "event.storeName", "eventBookmark.id")
				.containsExactly(Tuple.tuple(event.getId(), "event1", eventBookmark.getId()),
						Tuple.tuple(event2.getId(), "event2", eventBookmark2.getId()));
		assertThat(bookmarkedEvents.getTotalElements()).isEqualTo(2);
		assertThat(bookmarkedEvents.getTotalPages()).isEqualTo(1);

		assertThat(bookmarkedEvents2).hasSize(2)
				.extracting("event.id", "event.storeName", "eventBookmark.id")
				.containsExactlyInAnyOrder(Tuple.tuple(event.getId(), "event1", eventBookmark3.getId()),
						Tuple.tuple(event3.getId(), "event3", eventBookmark4.getId()));
		assertThat(bookmarkedEvents2.getTotalElements()).isEqualTo(3);
		assertThat(bookmarkedEvents2.getTotalPages()).isEqualTo(2);
	}

	private Event createEvent(User user, String storeName) {
		return Event.builder().user(user).storeName(storeName).build();
	}

	private EventBookmark createEventBookmark(User user, Event event, EventBookmarkFolder eventBookmarkFolder) {
		return EventBookmark.builder().user(user).event(event).eventBookmarkFolder(eventBookmarkFolder).build();
	}

	private EventBookmarkFolder createEventBookmarkFolder(String name) {
		return EventBookmarkFolder.builder().name(name).build();
	}
}
