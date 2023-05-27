package com.teamddd.duckmap.repository;

import static com.teamddd.duckmap.entity.QEvent.*;
import static com.teamddd.duckmap.entity.QEventArtist.*;
import static com.teamddd.duckmap.entity.QEventBookmark.*;
import static com.teamddd.duckmap.entity.QEventLike.*;
import static com.teamddd.duckmap.entity.QUser.*;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
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
	public EventLikeBookmarkDto findByIdWithLikeAndBookmark(Long eventId, Long userId) {
		return queryFactory.select(
				new QEventLikeBookmarkDto(
					event,
					eventLike,
					eventBookmark
				))
			.from(event)
			.leftJoin(eventLike).on(event.eq(eventLike.event).and(eventLikeUserEqUserId(userId)))
			.leftJoin(eventBookmark).on(event.eq(eventBookmark.event).and(eventBookmarkUserEqUserId(userId)))
			.where(event.id.eq(eventId))
			.fetchOne();
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
			.leftJoin(eventLike).on(event.eq(eventLike.event).and(event.user.id.eq(eventLike.user.id)))
			.leftJoin(eventBookmark).on(event.eq(eventBookmark.event).and(event.user.id.eq(eventBookmark.user.id)))
			.where(event.user.id.eq(userId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory.select(event.count())
			.from(event)
			.join(event.user, user)
			.where(user.id.eq(userId));

		return PageableExecutionUtils.getPage(events, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<EventLikeBookmarkDto> findByArtistAndDate(Long artistId, LocalDate date, Long userId,
		Pageable pageable) {
		JPAQuery<EventLikeBookmarkDto> eventsQuery = queryFactory.selectDistinct(
				new QEventLikeBookmarkDto(
					event,
					eventLike,
					eventBookmark
				))
			.from(event);
		if (artistId != null) {
			eventsQuery
				.leftJoin(event.eventArtists, eventArtist);
		}
		List<EventLikeBookmarkDto> events = eventsQuery
			.leftJoin(eventLike).on(event.eq(eventLike.event).and(eventLikeUserEqUserId(userId)))
			.leftJoin(eventBookmark).on(event.eq(eventBookmark.event).and(eventBookmarkUserEqUserId(userId)))
			.where(eqArtistId(artistId),
				betweenDate(date))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory.select(event.countDistinct())
			.from(event);
		if (artistId != null) {
			countQuery
				.leftJoin(event.eventArtists, eventArtist);
		}
		countQuery
			.where(eqArtistId(artistId),
				betweenDate(date));

		return PageableExecutionUtils.getPage(events, pageable, countQuery::fetchOne);
	}

	private BooleanExpression eventLikeUserEqUserId(Long userId) {
		return userId != null ? eventLike.user.id.eq(userId) : eventLike.user.id.isNull();
	}

	private BooleanExpression eventBookmarkUserEqUserId(Long userId) {
		return userId != null ? eventBookmark.user.id.eq(userId) : eventBookmark.user.id.isNull();
	}

	private BooleanExpression eqArtistId(Long artistId) {
		return artistId != null ? eventArtist.artist.id.eq(artistId) : null;
	}

	private BooleanExpression betweenDate(LocalDate date) {
		return date != null ? event.fromDate.loe(date).and(event.toDate.goe(date)) : null;
	}

}
