package com.teamddd.duckmap;

import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderEventDto;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventBookmark;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.User;
import com.teamddd.duckmap.repository.BookmarkFolderRepository;
import org.assertj.core.groups.Tuple;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

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
		em.persist(event);


		EventBookmark eventBookmark = createEventBookmark(user, event);
		em.persist(eventBookmark);

		EventBookmarkFolder eventBookmarkFolder1 = createEventBookmarkFolder(eventBookmark);
		em.persist(eventBookmarkFolder1);
		PageRequest pageRequest = PageRequest.of(0, 2);

		//when
		Page<BookmarkFolderEventDto> bookmarkedEvents = bookmarkFolderRepository.findBookmarkedEvents(eventBookmarkFolder1.getId(), pageRequest);

		//then
		assertThat(bookmarkedEvents).hasSize(1).extracting("event.id", "event.storeName", "eventBookmark.id").containsExactly(Tuple.tuple(event.getId(), "event1", eventBookmark.getId()));

		assertThat(bookmarkedEvents.getTotalElements()).isEqualTo(1);
		assertThat(bookmarkedEvents.getTotalPages()).isEqualTo(1);
	}

	private Event createEvent(User user, String storeName, LocalDate fromDate, LocalDate toDate, String hashtag) {
		return Event.builder().user(user).storeName(storeName).fromDate(fromDate).toDate(toDate).hashtag(hashtag).build();
	}

	private EventBookmark createEventBookmark(User user, Event event) {
		return EventBookmark.builder().user(user).event(event).build();
	}

	private EventBookmarkFolder createEventBookmarkFolder(EventBookmark bookmark) {
		return EventBookmarkFolder.builder().eventBookmarks(List.of(bookmark)).build();
	}
}
