package com.teamddd.duckmap.repository;

import static com.teamddd.duckmap.entity.QEvent.*;
import static com.teamddd.duckmap.entity.QEventBookmark.*;
import static com.teamddd.duckmap.entity.QEventLike.*;
import static com.teamddd.duckmap.entity.QUser.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teamddd.duckmap.dto.event.event.EventLikeBookmarkDto;
import com.teamddd.duckmap.dto.event.event.QEventLikeBookmarkDto;

public class EventRepositoryImpl implements EventRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public EventRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<EventLikeBookmarkDto> findMyEvents(Long userId, Pageable pageable) {
		List<EventLikeBookmarkDto> events = queryFactory.select(
				new QEventLikeBookmarkDto(
					event,
					eventLike,
					eventBookmark
				))
			.from(event)
			.join(event.user, user)
			.leftJoin(eventLike).on(event.eq(eventLike.event).and(user.eq(eventLike.user)))
			.leftJoin(eventBookmark).on(event.eq(eventBookmark.event).and(user.eq(eventBookmark.user)))
			.where(user.id.eq(userId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory.select(event.count())
			.from(event)
			.join(event.user, user)
			.where(user.id.eq(userId));

		return PageableExecutionUtils.getPage(events, pageable, countQuery::fetchOne);
	}
}
