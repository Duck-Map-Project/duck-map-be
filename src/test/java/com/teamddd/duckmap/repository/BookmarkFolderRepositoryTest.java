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

import com.teamddd.duckmap.dto.event.bookmark.BookmarkEventDto;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderMemberDto;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventBookmark;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.EventImage;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.NonExistentBookmarkFolderException;

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
		Member member = Member.builder().username("user1").build();
		Member member2 = Member.builder().username("user2").build();
		em.persist(member);
		em.persist(member2);

		Event event = createEvent(member, "event1");
		Event event2 = createEvent(member, "event2");
		em.persist(event);
		em.persist(event2);

		EventBookmarkFolder eventBookmarkFolder = createEventBookmarkFolder(member, "folder1");
		EventBookmarkFolder eventBookmarkFolder2 = createEventBookmarkFolder(member, "folder2");
		EventBookmarkFolder eventBookmarkFolder3 = createEventBookmarkFolder(member2, "folder3");
		em.persist(eventBookmarkFolder);
		em.persist(eventBookmarkFolder2);
		em.persist(eventBookmarkFolder3);

		EventBookmark eventBookmark = createEventBookmark(member, event, eventBookmarkFolder);
		EventBookmark eventBookmark2 = createEventBookmark(member, event2, eventBookmarkFolder2);
		EventBookmark eventBookmark3 = createEventBookmark(member2, event, eventBookmarkFolder3);

		em.persist(eventBookmark);
		em.persist(eventBookmark2);
		em.persist(eventBookmark3);

		PageRequest pageRequest = PageRequest.of(0, 2);
		//when
		Page<EventBookmarkFolder> bookmarkFolders = bookmarkFolderRepository
			.findBookmarkFoldersByMemberId(member.getId(), pageRequest);

		Page<EventBookmarkFolder> bookmarkFolders2 = bookmarkFolderRepository
			.findBookmarkFoldersByMemberId(member2.getId(), pageRequest);
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
		Member member = Member.builder().build();
		em.persist(member);

		Event event = createEvent(member, "event1");
		Event event2 = createEvent(member, "event2");
		Event event3 = createEvent(member, "event3");
		Event event4 = createEvent(member, "event4");
		em.persist(event);
		em.persist(event2);
		em.persist(event3);
		em.persist(event4);

		EventImage image1 = createEventImage(event, "image1", false);
		EventImage image2 = createEventImage(event, "image2", true);
		EventImage image3 = createEventImage(event2, "image3", true);
		EventImage image4 = createEventImage(event2, "image4", false);
		em.persist(image1);
		em.persist(image2);
		em.persist(image3);
		em.persist(image4);

		EventBookmarkFolder eventBookmarkFolder = createEventBookmarkFolder(member, "folder1");
		EventBookmarkFolder eventBookmarkFolder2 = createEventBookmarkFolder(member, "folder2");
		em.persist(eventBookmarkFolder);
		em.persist(eventBookmarkFolder2);

		EventBookmark eventBookmark = createEventBookmark(member, event, eventBookmarkFolder);
		EventBookmark eventBookmark2 = createEventBookmark(member, event2, eventBookmarkFolder);
		EventBookmark eventBookmark3 = createEventBookmark(member, event, eventBookmarkFolder2);
		EventBookmark eventBookmark4 = createEventBookmark(member, event3, eventBookmarkFolder2);
		EventBookmark eventBookmark5 = createEventBookmark(member, event4, eventBookmarkFolder2);
		em.persist(eventBookmark);
		em.persist(eventBookmark2);
		em.persist(eventBookmark3);
		em.persist(eventBookmark4);
		em.persist(eventBookmark5);

		PageRequest pageRequest = PageRequest.of(0, 2);
		//when
		Page<BookmarkEventDto> bookmarkedEvents = bookmarkFolderRepository
			.findBookmarkedEvents(eventBookmarkFolder.getId(), pageRequest);
		//then
		assertThat(bookmarkedEvents).hasSize(2)
			.extracting("eventId", "eventStoreName", "eventBookmarkId", "eventThumbnail")
			.containsExactly(
				Tuple.tuple(event.getId(), "event1", eventBookmark.getId(), "image2"),
				Tuple.tuple(event2.getId(), "event2", eventBookmark2.getId(), "image3"));
		assertThat(bookmarkedEvents.getTotalElements()).isEqualTo(2);
		assertThat(bookmarkedEvents.getTotalPages()).isEqualTo(1);
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

	EventImage createEventImage(Event event, String image, boolean thumbnail) {
		return EventImage.builder()
			.event(event)
			.image(image)
			.thumbnail(thumbnail)
			.build();
	}

	@DisplayName("북마크 폴더 pk로 북마크 폴더,사용자 정보 조회")
	@Test
	void findBookmarkFolderAndUserById() throws Exception {
		//given
		Member member = Member.builder().username("user1").build();
		Member member2 = Member.builder().username("user2").build();
		em.persist(member);
		em.persist(member2);

		Event event = createEvent(member, "event1");
		Event event2 = createEvent(member, "event2");
		em.persist(event);
		em.persist(event2);

		EventBookmarkFolder eventBookmarkFolder = createEventBookmarkFolder(member, "folder1");
		EventBookmarkFolder eventBookmarkFolder2 = createEventBookmarkFolder(member2, "folder2");
		em.persist(eventBookmarkFolder);
		em.persist(eventBookmarkFolder2);

		EventBookmark eventBookmark = createEventBookmark(member, event, eventBookmarkFolder);
		EventBookmark eventBookmark2 = createEventBookmark(member, event2, eventBookmarkFolder2);

		em.persist(eventBookmark);
		em.persist(eventBookmark2);

		//when
		BookmarkFolderMemberDto bookmarkFolderMemberDto = bookmarkFolderRepository
			.findBookmarkFolderAndMemberById(eventBookmarkFolder.getId())
			.orElseThrow(NonExistentBookmarkFolderException::new);
		BookmarkFolderMemberDto bookmarkFolderMemberDto2 = bookmarkFolderRepository
			.findBookmarkFolderAndMemberById(eventBookmarkFolder2.getId())
			.orElseThrow(NonExistentBookmarkFolderException::new);

		//then
		assertThat(bookmarkFolderMemberDto.getUsername()).isEqualTo("user1");
		assertThat(bookmarkFolderMemberDto.getBookmarkFolder().getName()).isEqualTo("folder1");
		assertThat(bookmarkFolderMemberDto2.getUsername()).isEqualTo("user2");
		assertThat(bookmarkFolderMemberDto2.getBookmarkFolder().getName()).isEqualTo("folder2");
	}

}
