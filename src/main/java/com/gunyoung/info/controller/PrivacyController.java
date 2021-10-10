package com.gunyoung.info.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.info.error.code.PrivacyPolicyErrorCode;
import com.gunyoung.info.error.exceptions.nonexist.PrivacyPolicyNotFoundedException;

/**
 * 개인 정보 처리방침을 제공하기 위한 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
public class PrivacyController {
	
	public static final int LATEST_POLICY_VERSION = 1;
	
	/**
	 * <pre>
	 *  - 기능: 가장 최근에 적용된 개인정보처리 방침 문서 화면 반환
	 *  - View: privacyPolicy_LATEST_POLICY_VERSION.html
	 *  </pre>
	 *  @author kimgun-yeong
	 *  
	 */
	@GetMapping(value="/privacypolicy")
	public ModelAndView privacyPolicyLastest(ModelAndView mav) {
		mav.setViewName("privacyPolicy_" +LATEST_POLICY_VERSION);
		
		return mav;
	}
	
	/**
	 * <pre>
	 *  - 기능: url에 표시된 버전의 개인정보처리 방침 문서 화면 반환 
	 *  - View: privacyPolicy_version.html  
	 *  </pre>
	 *  @param version 열람하려는 개인정보 처리방침의 버전 값
	 *  @throws PrivacyPolicyNotFoundedException 해당하는 버전의 개인정보 처리방침이 없을 때
	 *  @author kimgun-yeong
	 */
	@GetMapping(value="/privacypolicy/{version}")
	public ModelAndView privacyPolicyWithVersion(@PathVariable("version") int version,ModelAndView mav) {
		if(version <=0 || version > LATEST_POLICY_VERSION) {
			throw new PrivacyPolicyNotFoundedException(PrivacyPolicyErrorCode.PRIVACY_POLICY_VERSION_IS_NOT_VALID_ERROR.getDescription());
		}
		mav.setViewName("privacyPolicy_" +version);
		
		return mav;
	}
}
