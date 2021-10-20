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
	
	/**
	 * DB 정보를 통해 생성한 UserDetails와 입력된 Authentication을 비교하여 유효성을 검증하고 검증 성공 시 새로운 Authentication 반환 <br>
	 * @throws BadCredentialsException 유효성 검증 실패 시
	 * @author kimgun-yeong
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
