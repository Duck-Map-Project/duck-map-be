package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.event.bookmark.CreateBookmarkFolderReq;
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
}
