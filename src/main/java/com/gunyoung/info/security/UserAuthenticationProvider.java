package com.gunyoung.info.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	UserDetailsService userDetailService;

	
	/*
	 *  인자로 전해지는 authentication에는 사용자의 로그인 폼에서 입력값을 나타낸다.
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		 String email = authentication.getName();
	     
	     UserDetails authenticateResult= userDetailService.loadUserByUsername(email);
	     
	     return new UsernamePasswordAuthenticationToken(authenticateResult, null, authenticateResult.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
