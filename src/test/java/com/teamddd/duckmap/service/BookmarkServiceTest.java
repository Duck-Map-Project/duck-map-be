package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.bookmark.CreateBookmarkReq;
import com.teamddd.duckmap.dto.event.bookmark.UpdateBookmarkReq;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventBookmark;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.NonExistentBookmarkException;
import com.teamddd.duckmap.repository.BookmarkRepository;

@Transactional
@SpringBootTest
public class BookmarkServiceTest {
	@Autowired
	BookmarkService bookmarkService;
	@SpyBean
	BookmarkRepository bookmarkRepository;
	@MockBean
	EventService eventService;
	@MockBean
	BookmarkFolderService bookmarkFolderService;
	@Autowired
	EntityManager em;

	@DisplayName("이벤트 북마크 생성")
	@Test
	void createBookmark() throws Exception {
		//given
		Member member = Member.builder()
			.username("member1")
			.build();

		Event event = Event.builder()
			.storeName("store")
			.member(member)
			.build();

		EventBookmarkFolder bookmarkFolder = EventBookmarkFolder.builder()
			.image("image1")
			.name("folder1")
			.build();

		CreateBookmarkReq request = new CreateBookmarkReq();
		ReflectionTestUtils.setField(request, "bookmarkFolderId", bookmarkFolder.getId());

		when(eventService.getEvent(any())).thenReturn(event);
		when(bookmarkFolderService.getEventBookmarkFolder(any())).thenReturn(bookmarkFolder);

		//when
		Long bookmarkId = bookmarkService.createBookmark(event.getId(), request.getBookmarkFolderId(), member);

		//then
		assertThat(bookmarkId).isNotNull();

		Optional<EventBookmark> findBookmark = bookmarkRepository.findById(bookmarkId);
		assertThat(findBookmark).isNotEmpty();
		assertThat(findBookmark.get())
			.extracting("eventBookmarkFolder.name", "event.storeName", "member.username")
			.containsOnly("folder1", "store", "member1");
	}

	@DisplayName("이벤트 북마크 폴더 변경")
	@Test
	void updateBookmark() throws Exception {
		//given
		Member member = Member.builder()
			.username("member1")
			.build();
		em.persist(member);
		Event event = Event.builder()
			.storeName("store")
			.member(member)
			.build();
		em.persist(event);
		EventBookmarkFolder bookmarkFolder = EventBookmarkFolder.builder()
			.image("image1")
			.name("folder1")
			.build();
		em.persist(bookmarkFolder);
		EventBookmarkFolder bookmarkFolder2 = EventBookmarkFolder.builder()
			.image("image2")
			.name("folder2")
			.build();
		em.persist(bookmarkFolder2);

		when(eventService.getEvent(any())).thenReturn(event);
		when(bookmarkFolderService.getEventBookmarkFolder(bookmarkFolder.getId())).thenReturn(bookmarkFolder);
		Long bookmarkId = bookmarkService.createBookmark(event.getId(), bookmarkFolder.getId(), member);

		UpdateBookmarkReq request = new UpdateBookmarkReq();
		ReflectionTestUtils.setField(request, "bookmarkFolderId", bookmarkFolder2.getId());

		//when
		when(bookmarkFolderService.getEventBookmarkFolder(bookmarkFolder2.getId())).thenReturn(bookmarkFolder2);
		bookmarkService.updateBookmark(event.getId(), request, member);

		//then
		assertThat(bookmarkId).isNotNull();

		Optional<EventBookmark> findBookmark = bookmarkRepository.findById(bookmarkId);
		assertThat(findBookmark).isNotEmpty();
		assertThat(findBookmark.get())
			.extracting("eventBookmarkFolder.id", "eventBookmarkFolder.name", "event.storeName", "member.username")
			.containsOnly(bookmarkFolder2.getId(), "folder2", "store", "member1");
	}

	@DisplayName("이벤트 북마크 삭제(취소)")
	@Nested
	class DeleteBookmark {
		@DisplayName("로그인 사용자와 북마크한 사용자가 같을때")
		@Test
		void deleteBookmark1() throws Exception {
			//given
			Member member = Member.builder()
				.username("member1")
				.build();
			em.persist(member);

			Event event = Event.builder()
				.storeName("store")
				.member(member)
				.build();
			em.persist(event);

			EventBookmarkFolder bookmarkFolder = EventBookmarkFolder.builder()
				.image("image1")
				.name("folder1")
				.build();
			em.persist(bookmarkFolder);

			EventBookmark bookmark = EventBookmark.builder()
				.event(event)
				.eventBookmarkFolder(bookmarkFolder)
				.member(member)
				.build();
			em.persist(bookmark);

			//when
			bookmarkService.deleteBookmark(event.getId(), member.getId());

			//then
			Optional<EventBookmark> findBookmark = bookmarkRepository.findById(bookmark.getId());
			assertThat(findBookmark).isEmpty();
		}

		@DisplayName("로그인 사용자와 북마크한 사용자가 다를때")
		@Test
		void deleteBookmark2() throws Exception {
			//given
			Member member = Member.builder()
				.username("member1")
				.build();
			Member loginMember = Member.builder()
				.username("member2")
				.build();
			em.persist(member);
			em.persist(loginMember);

			Event event = Event.builder()
				.storeName("store")
				.member(member)
				.build();
			em.persist(event);

			EventBookmarkFolder bookmarkFolder = EventBookmarkFolder.builder()
				.image("image1")
				.name("folder1")
				.build();
			em.persist(bookmarkFolder);

			EventBookmark bookmark = EventBookmark.builder()
				.event(event)
				.eventBookmarkFolder(bookmarkFolder)
				.member(member)
				.build();
			em.persist(bookmark);

			//when //then
			assertThatThrownBy(() -> bookmarkService.deleteBookmark(event.getId(), loginMember.getId()))
				.isInstanceOf(NonExistentBookmarkException.class)
				.hasMessage("잘못된 북마크 정보입니다");
		}
	}

}
