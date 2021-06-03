package com.gunyoung.info.dto;

import javax.validation.constraints.NotEmpty;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;

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
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getGithub() {
		return github;
	}
	public void setGithub(String github) {
		this.github = github;
	}
	public String getInstagram() {
		return instagram;
	}
	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}
	public String getTweeter() {
		return tweeter;
	}
	public void setTweeter(String tweeter) {
		this.tweeter = tweeter;
	}
	public String getFacebook() {
		return facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
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
