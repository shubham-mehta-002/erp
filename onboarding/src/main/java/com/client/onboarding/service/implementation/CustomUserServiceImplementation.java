package com.client.onboarding.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.client.onboarding.model.User;
import com.client.onboarding.repository.UserRepository;



@Service
public class CustomUserServiceImplementation implements UserDetailsService {
//	private static final String List = null;
	private UserRepository userRepository;
	
	public CustomUserServiceImplementation(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}


	// to allow spring security framework to authenticate users based on data stored in the database
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("User not found with username -> "+ username);
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}

}
