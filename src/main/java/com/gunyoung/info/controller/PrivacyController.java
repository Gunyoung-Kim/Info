package com.gunyoung.info.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PrivacyController {
	
	public static final int LATEST_POLICY_VERSION = 1;
	
	/*
	 *  - 기능: 가장 최근에 적용된 개인정보처리 방침 문서를 보여주는 컨트롤러
	 *  - 반환: 
	 *  	- 성공
	 *  	View: privacyPolicy_LATEST_POLICY_VERSION.html
	 *  
	 */
	@RequestMapping(value="/privacypolicy", method = RequestMethod.GET)
	public ModelAndView privacyPolicyLastest(ModelAndView mav) {
		mav.setViewName("privacyPolicy_" +LATEST_POLICY_VERSION);
		
		return mav;
	}
	
	/*
	 *  - 기능: url에 표시된 버전의 개인정보처리 방침 문서를 보여주는 컨트롤러
	 *  - 반환: 
	 *  	- 성공
	 *  	View: privacyPolicy_version.html
	 *  	- 실패
	 *  	해당하는 버전의 개인정보 처리방침이 없을 
	 */
	@RequestMapping(value="/privacypolicy/{version}", method = RequestMethod.GET)
	public ModelAndView privacyPolicyWithVersion(@PathVariable("version") int version,ModelAndView mav) {
		if(version <=0 || version > LATEST_POLICY_VERSION) {
			return new ModelAndView("redirect:/errorpage");
		}
		mav.setViewName("privacyPolicy_" +version);
		
		return mav;
	}
}
