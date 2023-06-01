package com.teamddd.duckmap.config.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.teamddd.duckmap.entity.User;
import com.teamddd.duckmap.repository.UserRepository;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optional = userRepository.findByEmail(username);
		if (optional.isEmpty()) {
			throw new UsernameNotFoundException(username + " 사용자 없음");
		} else {
			User user = optional.get();
			return new SecurityUser(user);
		}

	}
}
