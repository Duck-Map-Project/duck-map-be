package com.teamddd.duckmap.repository;

import static com.teamddd.duckmap.entity.QEvent.*;
import static com.teamddd.duckmap.entity.QEventBookmark.*;

import java.util.Optional;

import javax.persistence.EntityManager;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkEventDto;
import com.teamddd.duckmap.dto.event.bookmark.QBookmarkEventDto;

public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public BookmarkRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Optional<BookmarkEventDto> findByEventAndMember(Long eventId, Long memberId) {
		return Optional.ofNullable(queryFactory.select(
				new QBookmarkEventDto(
					event,
					eventBookmark
				))
			.from(eventBookmark)
			.leftJoin(eventBookmark.event, event)
			.where(event.id.eq(eventId)
				.and(eventBookmark.member.id.eq(memberId)))
			.fetchOne());
	}
}
