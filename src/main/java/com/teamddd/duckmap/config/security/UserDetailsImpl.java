package com.teamddd.duckmap.config.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.teamddd.duckmap.entity.Member;

public class UserDetailsImpl implements UserDetails {
	private final Member member;

	public UserDetailsImpl(Member member) {
		this.member = member;
	}

	public Member getUser() {
		return member;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(() -> "ROLE_" + member.getRole().toString()); // key: ROLE_권한
		return authorities;
	}

	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getEmail();
	}

	// == 세부 설정 == //

	@Override
	public boolean isAccountNonExpired() { // 계정의 만료 여부
		return true;
	}

	@Override
	public boolean isAccountNonLocked() { // 계정의 잠김 여부
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() { // 비밀번호 만료 여부
		return true;
	}

	@Override
	public boolean isEnabled() { // 계정의 활성화 여부
		return true;
	}
}
