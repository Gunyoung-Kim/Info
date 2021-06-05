package com.gunyoung.info.dto;

public class MainListObject {
	private String personName;
	private String personEmail;
	
	public String getpersonName() {
		return personName;
	}
	public void setpersonName(String personName) {
		this.personName = personName;
	}
	public String getpersonEmail() {
		return personEmail;
	}
	public void setpersonEmail(String personEmail) {
		this.personEmail = personEmail;
	}
	
	public MainListObject() {
		
	}
	
	public MainListObject(String personName, String personEmail) {
		super();
		this.personName = personName;
		this.personEmail = personEmail;
	}
	
	
}
