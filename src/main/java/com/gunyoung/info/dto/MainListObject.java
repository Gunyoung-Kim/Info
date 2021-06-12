package com.gunyoung.info.dto;

import lombok.Data;

@Data
public class MainListObject {
	private String personName;
	private String personEmail;
	
	public MainListObject() {
		
	}
	
	public MainListObject(String personName, String personEmail) {
		super();
		this.personName = personName;
		this.personEmail = personEmail;
	}
	
	
}
