package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderMemberRes;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderRes;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkedEventRes;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkedEventsServiceReq;
import com.teamddd.duckmap.dto.event.bookmark.CreateBookmarkFolderReq;
import com.teamddd.duckmap.dto.event.bookmark.MyBookmarkFolderServiceReq;
import com.teamddd.duckmap.dto.event.bookmark.UpdateBookmarkFolderReq;
import com.teamddd.duckmap.entity.Event;
import com.teamddd.duckmap.entity.EventBookmark;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.EventImage;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.NonExistentBookmarkException;
import com.teamddd.duckmap.exception.NonExistentBookmarkFolderException;
import com.teamddd.duckmap.repository.BookmarkFolderRepository;

@Transactional
@SpringBootTest
public class BookmarkFolderServiceTest {
	@Autowired
	BookmarkFolderService bookmarkFolderService;
	@Autowired
	BookmarkService bookmarkService;
	@SpyBean
	BookmarkFolderRepository bookmarkFolderRepository;
	@Autowired
	EntityManager em;

	@DisplayName("북마크를 생성한다")
	@Test
	void createBookmarkFolder() throws Exception {
		//given
		CreateBookmarkFolderReq request = new CreateBookmarkFolderReq();
		ReflectionTestUtils.setField(request, "name", "bookmarkFolder1");
		ReflectionTestUtils.setField(request, "image", "image1");

		Member member = Member.builder()
			.username("member1")
			.build();

		//when
		Long bookmarkFolderId = bookmarkFolderService.createBookmarkFolder(request, member);

		//then
		assertThat(bookmarkFolderId).isNotNull();

		Optional<EventBookmarkFolder> findBookmarkFolder = bookmarkFolderRepository.findById(bookmarkFolderId);
		assertThat(findBookmarkFolder).isNotEmpty();
		assertThat(findBookmarkFolder.get())
			.extracting("name", "image", "member.username")
			.containsOnly("bookmarkFolder1", "image1", "member1");

	}

	@DisplayName("북마크 폴더명과 이미지를 변경한다")
	@Test
	void updateBookmarkFolder() throws Exception {
		//given
		CreateBookmarkFolderReq request = new CreateBookmarkFolderReq();
		ReflectionTestUtils.setField(request, "name", "bookmarkFolder1");
		ReflectionTestUtils.setField(request, "image", "image1");

		Member member = Member.builder()
			.username("member1")
			.build();

		Long bookmarkFolderId = bookmarkFolderService.createBookmarkFolder(request, member);

		UpdateBookmarkFolderReq request2 = new UpdateBookmarkFolderReq();
		ReflectionTestUtils.setField(request2, "name", "bookmarkFolder2");
		ReflectionTestUtils.setField(request2, "image", "image2");

		//when
		bookmarkFolderService.updateBookmarkFolder(bookmarkFolderId, request2);

		//then
		assertThat(bookmarkFolderId).isNotNull();

		Optional<EventBookmarkFolder> findBookmarkFolder = bookmarkFolderRepository.findById(bookmarkFolderId);
		assertThat(findBookmarkFolder).isNotEmpty();
		assertThat(findBookmarkFolder.get())
			.extracting("name", "image", "member.username")
			.containsOnly("bookmarkFolder2", "image2", "member1");
	}

	@DisplayName("북마크 폴더를 삭제한다")
	@Test
	void deleteBookmarkFolder() throws Exception {
		//given
		Member member = Member.builder()
			.username("member1")
			.build();
		em.persist(member);

		Event event = createEvent(member, "event1");
		Event event2 = createEvent(member, "event2");
		Event event3 = createEvent(member, "event3");
		em.persist(event);
		em.persist(event2);
		em.persist(event3);

		EventBookmarkFolder eventBookmarkFolder = createEventBookmarkFolder(member, "folder1");
		EventBookmarkFolder eventBookmarkFolder2 = createEventBookmarkFolder(member, "folder2");
		em.persist(eventBookmarkFolder);
		em.persist(eventBookmarkFolder2);

		EventBookmark eventBookmark = createEventBookmark(member, event, eventBookmarkFolder);
		EventBookmark eventBookmark2 = createEventBookmark(member, event2, eventBookmarkFolder);
		EventBookmark eventBookmark3 = createEventBookmark(member, event3, eventBookmarkFolder2);
		em.persist(eventBookmark);
		em.persist(eventBookmark2);
		em.persist(eventBookmark3);

		Pageable pageable = PageRequest.of(0, 4);

		MyBookmarkFolderServiceReq request = MyBookmarkFolderServiceReq.builder()
			.memberId(member.getId())
			.pageable(pageable)
			.build();

		//when
		bookmarkFolderService.deleteBookmarkFolder(eventBookmarkFolder.getId());

		//then
		Page<BookmarkFolderRes> myBookmarkFolders = bookmarkFolderService
			.getMyBookmarkFolderResList(request);
		assertThat(myBookmarkFolders).hasSize(1)
			.extracting("id", "name")
			.containsExactlyInAnyOrder(
				Tuple.tuple(eventBookmarkFolder2.getId(), "folder2"));

		//폴더 내 북마크 지워졌는지 확인
		assertThatThrownBy(() -> bookmarkService.getEventBookmark(event.getId(), member.getId()))
			.isInstanceOf(NonExistentBookmarkException.class)
			.hasMessage("잘못된 북마크 정보입니다");
		assertThatThrownBy(() -> bookmarkService.getEventBookmark(event2.getId(), member.getId()))
			.isInstanceOf(NonExistentBookmarkException.class)
			.hasMessage("잘못된 북마크 정보입니다");
	}

	@DisplayName("북마크 폴더 pk로 북마크 폴더 조회한다")
	@Nested
	class GetReview {
		@DisplayName("유효한 값으로 북마크 폴더 조회한다")
		@Test
		void getBookmarkFolder() throws Exception {
			//given
			CreateBookmarkFolderReq request = new CreateBookmarkFolderReq();
			ReflectionTestUtils.setField(request, "name", "bookmarkFolder1");
			ReflectionTestUtils.setField(request, "image", "image1");

			Member member = Member.builder()
				.username("member1")
				.build();
			em.persist(member);
			Long bookmarkFolderId = bookmarkFolderService.createBookmarkFolder(request, member);

			//when
			BookmarkFolderMemberRes bookmarkFolderMemberRes = bookmarkFolderService
				.getBookmarkFolderMemberRes(bookmarkFolderId);

			//then
			assertThat(bookmarkFolderMemberRes).extracting("name", "id", "username")
				.containsOnly("bookmarkFolder1", bookmarkFolderId, "member1");
		}

		@DisplayName("잘못된 값으로 북마크 폴더 조회할 수 없다")
		@Test
		void getBookmarkFolder2() throws Exception {
			//given
			Long bookmarkFolderId = 1L;

			//when //then
			assertThatThrownBy(() -> bookmarkFolderService.getBookmarkFolderMemberRes(bookmarkFolderId))
				.isInstanceOf(NonExistentBookmarkFolderException.class)
				.hasMessage("잘못된 북마크 폴더 정보입니다");
		}
	}

	@DisplayName("MemberId로 북마크 폴더 목록을 조회한다")
	@Test
	void getMyBookmarkFolders() throws Exception {
		//given
		Member member = Member.builder()
			.username("member1")
			.build();
		Member member2 = Member.builder()
			.username("member2")
			.build();
		em.persist(member);
		em.persist(member2);

		Event event = createEvent(member, "event1");
		Event event2 = createEvent(member, "event2");
		Event event3 = createEvent(member, "event3");
		em.persist(event);
		em.persist(event2);
		em.persist(event3);

		EventBookmarkFolder eventBookmarkFolder = createEventBookmarkFolder(member, "folder1");
		EventBookmarkFolder eventBookmarkFolder2 = createEventBookmarkFolder(member2, "folder2");
		EventBookmarkFolder eventBookmarkFolder3 = createEventBookmarkFolder(member, "folder3");
		EventBookmarkFolder eventBookmarkFolder4 = createEventBookmarkFolder(member, "folder4");
		em.persist(eventBookmarkFolder);
		em.persist(eventBookmarkFolder2);
		em.persist(eventBookmarkFolder3);
		em.persist(eventBookmarkFolder4);

		EventBookmark eventBookmark = createEventBookmark(member, event, eventBookmarkFolder);
		EventBookmark eventBookmark2 = createEventBookmark(member2, event2, eventBookmarkFolder2);
		EventBookmark eventBookmark3 = createEventBookmark(member, event2, eventBookmarkFolder3);
		EventBookmark eventBookmark4 = createEventBookmark(member, event3, eventBookmarkFolder4);
		em.persist(eventBookmark);
		em.persist(eventBookmark2);
		em.persist(eventBookmark3);
		em.persist(eventBookmark4);

		Pageable pageable = PageRequest.of(0, 4);

		MyBookmarkFolderServiceReq request = MyBookmarkFolderServiceReq.builder()
			.memberId(member.getId())
			.pageable(pageable)
			.build();

		//when
		Page<BookmarkFolderRes> myBookmarkFolders = bookmarkFolderService
			.getMyBookmarkFolderResList(request);

		//then
		assertThat(myBookmarkFolders).hasSize(3)
			.extracting("id", "name")
			.containsExactlyInAnyOrder(
				Tuple.tuple(eventBookmarkFolder.getId(), "folder1"),
				Tuple.tuple(eventBookmarkFolder3.getId(), "folder3"),
				Tuple.tuple(eventBookmarkFolder4.getId(), "folder4"));
	}

	@DisplayName("BookmarkFolderId로 북마크 폴더 내의 북마크된 이벤트 목록을 조회한다")
	@Test
	void getBookmarkedEvents() throws Exception {
		//given
		Member member = Member.builder()
			.username("member1")
			.build();
		em.persist(member);

		Event event = createEvent(member, "event1");
		Event event2 = createEvent(member, "event2");
		Event event3 = createEvent(member, "event3");
		em.persist(event);
		em.persist(event2);
		em.persist(event3);

		EventImage image1 = createEventImage(event, "image1", false);
		EventImage image2 = createEventImage(event, "image2", true);
		EventImage image3 = createEventImage(event2, "image3", true);
		EventImage image4 = createEventImage(event2, "image4", false);
		EventImage image5 = createEventImage(event3, "image5", true);
		em.persist(image1);
		em.persist(image2);
		em.persist(image3);
		em.persist(image4);
		em.persist(image5);

		EventBookmarkFolder eventBookmarkFolder = createEventBookmarkFolder(member, "folder1");
		em.persist(eventBookmarkFolder);

		EventBookmark eventBookmark = createEventBookmark(member, event, eventBookmarkFolder);
		EventBookmark eventBookmark2 = createEventBookmark(member, event2, eventBookmarkFolder);
		EventBookmark eventBookmark3 = createEventBookmark(member, event3, eventBookmarkFolder);
		em.persist(eventBookmark);
		em.persist(eventBookmark2);
		em.persist(eventBookmark3);

		Pageable pageable = PageRequest.of(0, 4);

		BookmarkedEventsServiceReq request = BookmarkedEventsServiceReq.builder()
			.bookmarkFolderId(eventBookmarkFolder.getId())
			.pageable(pageable)
			.build();

		//when
		Page<BookmarkedEventRes> bookmarkedEvents = bookmarkFolderService
			.getBookmarkedEventResList(request);

		//then
		assertThat(bookmarkedEvents).hasSize(3)
			.extracting("eventId", "storeName", "id", "image")
			.containsExactlyInAnyOrder(
				Tuple.tuple(event.getId(), "event1", eventBookmark.getId(), "/images/image2"),
				Tuple.tuple(event2.getId(), "event2", eventBookmark2.getId(), "/images/image3"),
				Tuple.tuple(event3.getId(), "event3", eventBookmark3.getId(), "/images/image5"));

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
}
