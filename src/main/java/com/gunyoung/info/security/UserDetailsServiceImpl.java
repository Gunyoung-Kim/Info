package com.gunyoung.info.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.services.domain.PersonService;

import lombok.RequiredArgsConstructor;

@Service("userDetailsService")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final PersonService personService;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Person person = personService.findByEmail(email);
		if(person == null)
			throw new UsernameNotFoundException("User not found with: " + email);
		UserDetails userDetails = new UserDetailsVO(person.getEmail(),person.getPassword(),person.getRole());
		
		return userDetails; 
	}

}
