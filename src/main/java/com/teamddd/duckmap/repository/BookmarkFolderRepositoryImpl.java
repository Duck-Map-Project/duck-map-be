package com.teamddd.duckmap.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkFolderEventDto;
import com.teamddd.duckmap.dto.event.bookmark.QBookmarkFolderEventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.teamddd.duckmap.entity.QEvent.event;
import static com.teamddd.duckmap.entity.QEventBookmark.eventBookmark;
import static com.teamddd.duckmap.entity.QEventBookmarkFolder.eventBookmarkFolder;

public class BookmarkFolderRepositoryImpl implements BookmarkFolderRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public BookmarkFolderRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<BookmarkFolderEventDto> findBookmarkedEvents(Long bookmarkFolderId, Pageable pageable) {
		List<BookmarkFolderEventDto> events = queryFactory.select(
						new QBookmarkFolderEventDto(
								event,
								eventBookmark
						))
				.from(event)
				.leftJoin(eventBookmark).on(event.eq(eventBookmark.event))
				.leftJoin(eventBookmark.eventBookmarkFolder, eventBookmarkFolder)
				.on(eventBookmarkFolder.id.eq(bookmarkFolderId))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> countQuery = queryFactory.select(event.count())
				.from(event)
				.leftJoin(eventBookmark).on(event.eq(eventBookmark.event))
				.leftJoin(eventBookmark.eventBookmarkFolder, eventBookmarkFolder)
				.on(eventBookmarkFolder.id.eq(bookmarkFolderId));

		return PageableExecutionUtils.getPage(events, pageable, countQuery::fetchOne);
	}

}
