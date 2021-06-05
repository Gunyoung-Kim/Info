package com.gunyoung.info.dto;

public class MainListObject {
	private String personName;
	private String personEmail;
	
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getPersonEmail() {
		return personEmail;
	}
	public void setPersonEmail(String personEmail) {
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
