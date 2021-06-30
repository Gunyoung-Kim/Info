package com.gunyoung.info.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider {
	
	private final UserDetailsService userDetailService;
	
	private final PasswordEncoder passwordEncoder;
	
	/*
	 *  인자로 전해지는 authentication에는 사용자의 로그인 폼에서 입력값을 나타낸다.
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		 String email = authentication.getName();
		 String password = (String) authentication.getCredentials();
	     
	     UserDetails userDetailsFromDB= userDetailService.loadUserByUsername(email);
	     
	     if(!passwordEncoder.matches(password, userDetailsFromDB.getPassword())) {
	    	 throw new BadCredentialsException(email);
	     }
	     
	     return new UsernamePasswordAuthenticationToken(userDetailsFromDB.getUsername(), userDetailsFromDB.getPassword(), userDetailsFromDB.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
