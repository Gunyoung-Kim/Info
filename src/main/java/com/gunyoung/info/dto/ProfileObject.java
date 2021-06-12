package com.gunyoung.info.dto;

import javax.validation.constraints.NotEmpty;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;

import lombok.Data;

@Data
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
	
	public ProfileObject() {
		
	}
	
	public void settingByPersonAndSpace(Person person, Space space) {
		this.email = person.getEmail();
		this.firstName = person.getFirstName();
		this.lastName = person.getLastName();
		this.description = space.getDescription();
		this.github = space.getGithub();
		this.tweeter = space.getTweeter();
		this.facebook = space.getFacebook();
		this.instagram = space.getInstagram();
	}
	
}
