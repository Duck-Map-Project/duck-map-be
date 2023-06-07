package com.teamddd.duckmap.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String email;
	private String password;
	private String image;

	@Enumerated(EnumType.STRING)
	private Role role; //USER, ADMIN

	private LocalDateTime loginAt;

	public void updateMemberInfo(String newUsername, String image) {
		this.username = newUsername;
		this.image = image;
	}

	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}
}
