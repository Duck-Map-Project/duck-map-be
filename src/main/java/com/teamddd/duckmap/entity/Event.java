package com.teamddd.duckmap.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Getter
@Table(name = "event_info")
public class Event extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String storeName;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String address;
	private String businessHour;
	private String hashtag;
	private String twitterUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Builder.Default
	@OneToMany(mappedBy = "event", cascade = CascadeType.PERSIST)
	private List<EventArtist> eventArtists = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "event", cascade = CascadeType.PERSIST)
	private List<EventInfoCategory> eventInfoCategories = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "event", cascade = CascadeType.PERSIST)
	private List<EventImage> eventImages = new ArrayList<>();

	public boolean isInProgress(LocalDate date) {
		return (date.isEqual(fromDate) || date.isAfter(fromDate))
			&& (date.isEqual(toDate) || date.isBefore(toDate));
	}

	public void updateEvent(String storeName, LocalDate fromDate, LocalDate toDate, String address, String businessHour,
		String hashtag, String twitterUrl) {
		this.storeName = storeName;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.address = address;
		this.businessHour = businessHour;
		this.hashtag = hashtag;
		this.twitterUrl = twitterUrl;
	}
}
