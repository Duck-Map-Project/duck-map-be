package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderEventDto;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderUserDto;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventBookmark;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.User;

@SpringBootTest
@Transactional
public class BookmarkFolderRepositoryTest {
	@Autowired
	BookmarkFolderRepository bookmarkFolderRepository;
	@Autowired
	EntityManager em;

	@DisplayName("UserId로 북마크 폴더 목록 조회")
	@Test
	public void findBookmarkFoldersByUserId() throws Exception {
		//given
		User user = User.builder().username("user1").build();
		User user2 = User.builder().username("user2").build();
		em.persist(user);
		em.persist(user2);

		Event event = createEvent(user, "event1");
		Event event2 = createEvent(user, "event2");
		em.persist(event);
		em.persist(event2);

		EventBookmarkFolder eventBookmarkFolder = createEventBookmarkFolder(user, "folder1");
		EventBookmarkFolder eventBookmarkFolder2 = createEventBookmarkFolder(user, "folder2");
		EventBookmarkFolder eventBookmarkFolder3 = createEventBookmarkFolder(user2, "folder3");
		em.persist(eventBookmarkFolder);
		em.persist(eventBookmarkFolder2);
		em.persist(eventBookmarkFolder3);

		EventBookmark eventBookmark = createEventBookmark(user, event, eventBookmarkFolder);
		EventBookmark eventBookmark2 = createEventBookmark(user, event2, eventBookmarkFolder2);
		EventBookmark eventBookmark3 = createEventBookmark(user2, event, eventBookmarkFolder3);

		em.persist(eventBookmark);
		em.persist(eventBookmark2);
		em.persist(eventBookmark3);

		PageRequest pageRequest = PageRequest.of(0, 2);
		//when
		Page<EventBookmarkFolder> bookmarkFolders = bookmarkFolderRepository
			.findBookmarkFoldersByUserId(user.getId(), pageRequest);

		Page<EventBookmarkFolder> bookmarkFolders2 = bookmarkFolderRepository
			.findBookmarkFoldersByUserId(user2.getId(), pageRequest);
		//then
		assertThat(bookmarkFolders).hasSize(2)
			.extracting("name")
			.containsExactlyInAnyOrder("folder1", "folder2");
		assertThat(bookmarkFolders.getTotalElements()).isEqualTo(2);
		assertThat(bookmarkFolders.getTotalPages()).isEqualTo(1);

		assertThat(bookmarkFolders2).hasSize(1)
			.extracting("name")
			.containsExactlyInAnyOrder("folder3");
		assertThat(bookmarkFolders2.getTotalElements()).isEqualTo(1);
		assertThat(bookmarkFolders2.getTotalPages()).isEqualTo(1);
	}

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

		EventBookmarkFolder eventBookmarkFolder = createEventBookmarkFolder(user, "folder1");
		EventBookmarkFolder eventBookmarkFolder2 = createEventBookmarkFolder(user, "folder2");
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

	private EventBookmarkFolder createEventBookmarkFolder(User user, String name) {
		return EventBookmarkFolder.builder().user(user).name(name).build();
	}

	@DisplayName("북마크 폴더 pk로 북마크 폴더,사용자 정보 조회")
	@Test
	void findBookmarkFolderAndUserById() throws Exception {
		//given
		User user = User.builder().username("user1").build();
		User user2 = User.builder().username("user2").build();
		em.persist(user);
		em.persist(user2);

		Event event = createEvent(user, "event1");
		Event event2 = createEvent(user, "event2");
		em.persist(event);
		em.persist(event2);

		EventBookmarkFolder eventBookmarkFolder = createEventBookmarkFolder(user, "folder1");
		EventBookmarkFolder eventBookmarkFolder2 = createEventBookmarkFolder(user2, "folder2");
		em.persist(eventBookmarkFolder);
		em.persist(eventBookmarkFolder2);

		EventBookmark eventBookmark = createEventBookmark(user, event, eventBookmarkFolder);
		EventBookmark eventBookmark2 = createEventBookmark(user, event2, eventBookmarkFolder2);

		em.persist(eventBookmark);
		em.persist(eventBookmark2);

		//when
		BookmarkFolderUserDto bookmarkFolderUserDto = bookmarkFolderRepository
			.findBookmarkFolderAndUserById(eventBookmarkFolder.getId());
		BookmarkFolderUserDto bookmarkFolderUserDto2 = bookmarkFolderRepository
			.findBookmarkFolderAndUserById(eventBookmarkFolder2.getId());

		//then
		assertThat(bookmarkFolderUserDto.getUsername()).isEqualTo("user1");
		assertThat(bookmarkFolderUserDto.getBookmarkFolder().getName()).isEqualTo("folder1");
		assertThat(bookmarkFolderUserDto2.getUsername()).isEqualTo("user2");
		assertThat(bookmarkFolderUserDto2.getBookmarkFolder().getName()).isEqualTo("folder2");
	}

}
