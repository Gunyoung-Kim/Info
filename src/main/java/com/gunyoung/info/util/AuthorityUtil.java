package com.gunyoung.info.util;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthorityUtil {
	
	/**
	 * SecurityContext에 저장된 유저의 이메일을 반환 <br>
	 * Authentication name 이 email
	 * @author kimgun-yeong
	 */
	public static String getSessionUserEmail() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		
		return auth.getName();
	}
	
	/**
	 * SecurityContext에 저장된 유저의 Authorities 반환
	 * @author kimgun-yeong
	 */
	public static Collection<? extends GrantedAuthority> getSessionUserAuthorities() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		
		return auth.getAuthorities();
	}
	
}
