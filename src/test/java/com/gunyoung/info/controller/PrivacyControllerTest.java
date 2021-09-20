package com.gunyoung.info.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.gunyoung.info.testutil.Integration;

/**
 * {@link PrivacyController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
@AutoConfigureMockMvc
public class PrivacyControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	/*
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value="/privacypolicy/{version}", method = RequestMethod.GET)
	 *		public ModelAndView privacyPolicyWithVersion(@PathVariable("version") int version,ModelAndView mav)
	 */
	@Test
	@DisplayName("개인정보 처리방침 페이지 with 버전 (실패- 해당 버전의 개인정보 처리방침이 존재하지 않을 때)")
	public void privacyPolicyWithVersionNoVersion() throws Exception {
		//Given
		int nonExistVersion = PrivacyController.LATEST_POLICY_VERSION + 10;
		
		//When
		mockMvc.perform(get("/privacypolicy/" + nonExistVersion))
		
		//Then
				.andExpect(status().isNoContent());
	}
}
