package com.teamddd.duckmap;

import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderEventDto;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventBookmark;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.User;
import com.teamddd.duckmap.repository.BookmarkFolderRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;

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

		Event event = createEvent(user, "event1", LocalDate.now(), LocalDate.now(), "#hashtag");
		Event event2 = createEvent(user, "event2", LocalDate.now(), LocalDate.now(), "#hashtag");
		Event event3 = createEvent(user, "event3", LocalDate.now(), LocalDate.now(), "#hashtag");
		em.persist(event);
		em.persist(event2);
		em.persist(event3);

		EventBookmarkFolder eventBookmarkFolder = createEventBookmarkFolder();
		em.persist(eventBookmarkFolder);

		EventBookmark eventBookmark = createEventBookmark(user, event, eventBookmarkFolder);
		EventBookmark eventBookmark2 = createEventBookmark(user, event2, eventBookmarkFolder);
		EventBookmark eventBookmark3 = createEventBookmark(user, event3, eventBookmarkFolder);

		eventBookmark.getEventBookmarkFolder().getEventBookmarks().add(eventBookmark);
		eventBookmark2.getEventBookmarkFolder().getEventBookmarks().add(eventBookmark2);
		eventBookmark3.getEventBookmarkFolder().getEventBookmarks().add(eventBookmark3);

		em.persist(eventBookmark);
		em.persist(eventBookmark2);
		em.persist(eventBookmark3);

		PageRequest pageRequest = PageRequest.of(0, 3);

		//when
		Page<BookmarkFolderEventDto> bookmarkedEvents = bookmarkFolderRepository
				.findBookmarkedEvents(eventBookmarkFolder.getId(), pageRequest);

		//then
		assertThat(bookmarkedEvents).hasSize(3)
				.extracting("event.id", "event.storeName", "eventBookmark.id")
				.containsExactly(Tuple.tuple(event.getId(), "event1", eventBookmark.getId()),
						Tuple.tuple(event2.getId(), "event2", eventBookmark2.getId()),
						Tuple.tuple(event3.getId(), "event3", eventBookmark3.getId()));

		assertThat(bookmarkedEvents.getTotalElements()).isEqualTo(3);
		assertThat(bookmarkedEvents.getTotalPages()).isEqualTo(1);
	}

	private Event createEvent(User user, String storeName, LocalDate fromDate, LocalDate toDate, String hashtag) {
		return Event.builder().user(user).storeName(storeName).fromDate(fromDate).toDate(toDate).hashtag(hashtag).build();
	}

	private EventBookmark createEventBookmark(User user, Event event, EventBookmarkFolder eventBookmarkFolder) {
		return EventBookmark.builder().user(user).event(event).eventBookmarkFolder(eventBookmarkFolder).build();
	}

	private EventBookmarkFolder createEventBookmarkFolder() {
		return EventBookmarkFolder.builder().eventBookmarks(new ArrayList<>()).build();
	}
}
