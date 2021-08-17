package com.gunyoung.info.dto.oauth2;

import com.gunyoung.info.domain.Person;

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
	
	/**
	 * OAuth2Join에 담긴 정보, 인코징된 패스워드를 통해 Person 객체 생성 후 반환
	 * @author kimgun-yeong
	 */
	public Person createPersonFromOAuth2Join(String encodedPassword) {
		Person person = Person.builder()
				.email(this.email)
				.password(encodedPassword)
				.firstName(this.firstName)
				.lastName(this.lastName)
				.build();
		
		return person;
	}
}
