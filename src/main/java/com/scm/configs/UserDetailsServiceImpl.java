package com.scm.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.scm.dao.SUserRepository;
import com.scm.models.SUser;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private SUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SUser userByUsername = userRepository.getUserByUsername(username);
		if (userByUsername == null) {
			throw new UsernameNotFoundException("User with username:" + username + " not found");
		}

		CustomUserDetails customUserDetails = new CustomUserDetails(userByUsername);

		return customUserDetails;
	}

}
