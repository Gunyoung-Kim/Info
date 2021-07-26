package com.gunyoung.info.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
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
		int latest_version = PrivacyController.LATEST_POLICY_VERSION;
		mockMvc.perform(get("/privacypolicy/" + (latest_version+3)))
				.andExpect(status().isNoContent());
	}
}
