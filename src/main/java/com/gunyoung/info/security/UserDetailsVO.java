package com.gunyoung.info.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gunyoung.info.enums.RoleType;

import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@NoArgsConstructor
public class UserDetailsVO implements UserDetails {

	private String email;
	private String password;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserDetailsVO(String email,String password,RoleType role) {
		this.email = email;
		this.password = password;
		if(role == RoleType.USER)
			this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		else 
			this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}
	
	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
