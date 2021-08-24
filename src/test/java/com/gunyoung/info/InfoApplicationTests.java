package com.gunyoung.info;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InfoApplicationTests {
	
	@Test
	@DisplayName("Spring context load 테스트")
	void contextLoads() {
		//Given
		String[] args = {};
		
		//When
		InfoApplication.main(args);
		
		//Then
	}

}
