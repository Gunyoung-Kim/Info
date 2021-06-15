package com.gunyoung.info.dto.oauth2;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuth2Join {
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	
	public  OAuth2Join() {
	}

	public OAuth2Join(String email, String password, String firstName, String lastName) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	
}
