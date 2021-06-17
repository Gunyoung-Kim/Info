package com.gunyoung.info.dto;

import lombok.Data;

/**
 * 메인 화면에 나타나는 리스트의 내용을 전달하기 위한 DTO 객체
 * @author kimgun-yeong
 *
 */
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
