package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.bookmark.CreateBookmarkFolderReq;
import com.teamddd.duckmap.dto.event.bookmark.UpdateBookmarkFolderReq;
import com.teamddd.duckmap.entity.EventBookmarkFolder;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.repository.BookmarkFolderRepository;

@Transactional
@SpringBootTest
public class BookmarkFolderServiceTest {
	@Autowired
	BookmarkFolderService bookmarkFolderService;
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
}
