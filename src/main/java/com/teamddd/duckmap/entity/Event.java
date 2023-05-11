package com.teamddd.duckmap.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String storeName;
	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	private String address;
	private String businessHour;
	private String hashtag;
	private String twitterUrl;
	private LocalDateTime createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "event")
	private List<EventArtist> eventArtists = new ArrayList<>();

	@OneToMany(mappedBy = "event")
	private List<EventInfoCategory> eventInfoCategories = new ArrayList<>();

	@OneToMany(mappedBy = "event")
	private List<EventImage> eventImages = new ArrayList<>();
}
