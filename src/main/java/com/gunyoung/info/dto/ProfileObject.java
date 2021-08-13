package com.gunyoung.info.dto;

import javax.validation.constraints.NotEmpty;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Person의 일부 필드 값 + Space의 일부 필드 값 (개인 포트폴리오 정보)을 변경하기 위한 DTO 객체
 * @author kimgun-yeong
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileObject {
	@NotEmpty
	private String email;
	
	@NotEmpty
	private String firstName;
	
	@NotEmpty
	private String lastName;
	
	private String description;
	
	private String github;
	
	private String instagram;
	
	private String tweeter;
	
	private String facebook;
	
	/**
	 * Person, Space 를 통해 ProfileObject 생성 후 반환
	 * @author kimgun-yeong
	 */
	public static ProfileObject createFromPersonAndSpace(Person person, Space space) {
		ProfileObject profileObject = ProfileObject.builder()
				.email(person.getEmail())
				.firstName(person.getFirstName())
				.lastName(person.getLastName())
				.description(space.getDescription())
				.github(space.getGithub())
				.instagram(space.getInstagram())
				.tweeter(space.getTweeter())
				.facebook(space.getFacebook())
				.build();
		
		return profileObject;
	}
}
