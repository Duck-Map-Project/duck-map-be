package com.teamddd.duckmap.repository;

import static com.teamddd.duckmap.entity.QArtist.*;
import static com.teamddd.duckmap.entity.QEvent.*;
import static com.teamddd.duckmap.entity.QEventArtist.*;
import static com.teamddd.duckmap.entity.QEventBookmark.*;
import static com.teamddd.duckmap.entity.QEventLike.*;
import static com.teamddd.duckmap.entity.QMember.*;
import static com.teamddd.duckmap.entity.QReview.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teamddd.duckmap.dto.event.event.EventLikeBookmarkDto;
import com.teamddd.duckmap.dto.event.event.EventLikeReviewCountDto;
import com.teamddd.duckmap.dto.event.event.QEventLikeBookmarkDto;
import com.teamddd.duckmap.dto.event.event.QEventLikeReviewCountDto;

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
		List<EventLikeBookmarkDto> events = queryFactory.selectDistinct(
				new QEventLikeBookmarkDto(
					event,
					eventLike.id,
					eventBookmark.id
				))
			.from(event)
			.join(event.eventArtists, eventArtist)
			.join(eventArtist.artist, artist)
			.leftJoin(eventLike).on(event.eq(eventLike.event).and(eventLikeMemberEqMemberId(memberId)))
			.leftJoin(eventBookmark).on(event.eq(eventBookmark.event).and(eventBookmarkMemberEqMemberId(memberId)))
			.where(eqArtistId(artistId),
				betweenDate(date))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory.select(event.countDistinct())
			.from(event)
			.join(event.eventArtists, eventArtist)
			.join(eventArtist.artist, artist)
			.where(eqArtistId(artistId),
				betweenDate(date));

		return PageableExecutionUtils.getPage(events, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<EventLikeReviewCountDto> findForMap(LocalDate date, Pageable pageable) {
		List<EventLikeReviewCountDto> events = queryFactory.select(new QEventLikeReviewCountDto(
				event,
				eventLike.countDistinct().as("likeCount"),
				review.countDistinct().as("reviewCount")
			)).from(event)
			.leftJoin(eventLike).on(event.eq(eventLike.event))
			.leftJoin(review).on(event.eq(review.event))
			.join(event.eventArtists, eventArtist)
			.join(eventArtist.artist, artist)
			.where(betweenDate(date))
			.groupBy(event)
			.orderBy(itemOrderBySort(pageable.getSort()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory.select(event.countDistinct())
			.from(event)
			.join(event.eventArtists, eventArtist)
			.join(eventArtist.artist, artist)
			.where(betweenDate(date));

		return PageableExecutionUtils.getPage(events, pageable, countQuery::fetchOne);
	}

	private OrderSpecifier[] itemOrderBySort(Sort sort) {
		List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

		PathBuilder pathBuilder;
		for (Sort.Order order : sort) {
			Order sortBy = order.isAscending() ? Order.ASC : Order.DESC;
			pathBuilder = new PathBuilder(EventLikeReviewCountDto.class, order.getProperty());
			orderSpecifiers.add(new OrderSpecifier(sortBy, pathBuilder));
		}

		return orderSpecifiers.toArray(OrderSpecifier[]::new);
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
