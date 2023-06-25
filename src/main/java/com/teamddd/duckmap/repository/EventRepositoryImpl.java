package com.teamddd.duckmap.repository;

import static com.teamddd.duckmap.entity.QEvent.*;
import static com.teamddd.duckmap.entity.QEventArtist.*;
import static com.teamddd.duckmap.entity.QEventBookmark.*;
import static com.teamddd.duckmap.entity.QEventLike.*;
import static com.teamddd.duckmap.entity.QMember.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
	public Optional<EventLikeBookmarkDto> findByIdWithLikeAndBookmark(Long eventId, Long memberId) {
		return Optional.ofNullable(queryFactory.select(
				new QEventLikeBookmarkDto(
					event,
					eventLike.id,
					eventBookmark.id
				))
			.from(event)
			.leftJoin(eventLike).on(event.eq(eventLike.event).and(eventLikeMemberEqMemberId(memberId)))
			.leftJoin(eventBookmark).on(event.eq(eventBookmark.event).and(eventBookmarkMemberEqMemberId(memberId)))
			.where(event.id.eq(eventId))
			.fetchOne());
	}

	@Override
	public Page<EventLikeBookmarkDto> findMyEvents(Long memberId, Pageable pageable) {
		List<EventLikeBookmarkDto> events = queryFactory.select(
				new QEventLikeBookmarkDto(
					event,
					eventLike.id,
					eventBookmark.id
				))
			.from(event)
			.leftJoin(eventLike).on(event.eq(eventLike.event).and(event.member.id.eq(eventLike.member.id)))
			.leftJoin(eventBookmark).on(event.eq(eventBookmark.event).and(event.member.id.eq(eventBookmark.member.id)))
			.where(event.member.id.eq(memberId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory.select(event.count())
			.from(event)
			.join(event.member, member)
			.where(member.id.eq(memberId));

		return PageableExecutionUtils.getPage(events, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<EventLikeBookmarkDto> findMyLikeEvents(Long memberId, Pageable pageable) {
		List<EventLikeBookmarkDto> events = queryFactory.select(
				new QEventLikeBookmarkDto(
					event,
					eventLike.id,
					eventBookmark.id
				))
			.from(event)
			.join(eventLike).on(event.eq(eventLike.event).and(eventLike.member.id.eq(memberId)))
			.leftJoin(eventBookmark).on(event.eq(eventBookmark.event).and(event.member.id.eq(eventBookmark.member.id)))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory.select(event.count())
			.from(event)
			.join(eventLike).on(event.eq(eventLike.event).and(eventLike.member.id.eq(memberId)));

		return PageableExecutionUtils.getPage(events, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<EventLikeBookmarkDto> findByArtistAndDate(Long artistId, LocalDate date, Long memberId,
		Pageable pageable) {
		JPAQuery<EventLikeBookmarkDto> eventsQuery = queryFactory.selectDistinct(
				new QEventLikeBookmarkDto(
					event,
					eventLike.id,
					eventBookmark.id
				))
			.from(event);
		if (artistId != null) {
			eventsQuery
				.leftJoin(event.eventArtists, eventArtist);
		}
		List<EventLikeBookmarkDto> events = eventsQuery
			.leftJoin(eventLike).on(event.eq(eventLike.event).and(eventLikeMemberEqMemberId(memberId)))
			.leftJoin(eventBookmark).on(event.eq(eventBookmark.event).and(eventBookmarkMemberEqMemberId(memberId)))
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

	private BooleanExpression eventLikeMemberEqMemberId(Long memberId) {
		return memberId != null ? eventLike.member.id.eq(memberId) : eventLike.member.id.isNull();
	}

	private BooleanExpression eventBookmarkMemberEqMemberId(Long memberId) {
		return memberId != null ? eventBookmark.member.id.eq(memberId) : eventBookmark.member.id.isNull();
	}

	private BooleanExpression eqArtistId(Long artistId) {
		return artistId != null ? eventArtist.artist.id.eq(artistId) : null;
	}

	private BooleanExpression betweenDate(LocalDate date) {
		return date != null ? event.fromDate.loe(date).and(event.toDate.goe(date)) : null;
	}

}
