package com.gunyoung.info.security;

@SuppressWarnings("serial")
public class UserNotFoundException extends RuntimeException {
	
	public UserNotFoundException(String userEmail){ 
		super(userEmail + " NotFoundException"); 
	}
}
