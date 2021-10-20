package com.gunyoung.info.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.gunyoung.info.domain.Person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinDTO {
	
	@NotEmpty(message = "{email.notEmpty}")
	@Size(max = 50)
	@Email(message = "{email.email}")
	private String email;
	
	@NotEmpty(message = "{password.notEmpty}")
	private String password;
	
	@NotEmpty(message = "{firstName.notEmpty}")
	@Size(max = 60, message = "{firstName.size}")
	private String firstName;
	
	@NotEmpty(message = "{lastName.notEmpty}")
	@Size(max = 60, message = "{lastName.size}")
	private String lastName;
	
	/**
	 * 해당 객체의 필드를 통해 Person 객체 생성 후 반환
	 * @author kimgun-yeong
	 */
	public Person createPerson() {
		return Person.builder()
				.email(this.email)
				.password(this.password)
				.firstName(this.firstName)
				.lastName(this.lastName)
				.build();
	}
}
