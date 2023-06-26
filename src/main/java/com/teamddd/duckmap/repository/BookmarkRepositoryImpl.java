package com.teamddd.duckmap.repository;

import static com.teamddd.duckmap.entity.QEvent.*;
import static com.teamddd.duckmap.entity.QEventBookmark.*;

import java.util.Optional;

import javax.persistence.EntityManager;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teamddd.duckmap.dto.event.bookmark.BookmarkEventDto;
import com.teamddd.duckmap.dto.event.bookmark.QBookmarkEventDto;

public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public BookmarkRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Optional<BookmarkEventDto> findByIdWithEvent(Long eventId, Long memberId) {
		return Optional.ofNullable(queryFactory.select(
				new QBookmarkEventDto(
					event,
					eventBookmark
				))
			.from(event)
			.leftJoin(eventBookmark).on(event.eq(eventBookmark.event).and(eventBookmarkMemberEqMemberId(memberId)))
			.where(event.id.eq(eventId))
			.fetchOne());
	}

	private BooleanExpression eventBookmarkMemberEqMemberId(Long memberId) {
		return memberId != null ? eventBookmark.member.id.eq(memberId) : eventBookmark.member.id.isNull();
	}
}
