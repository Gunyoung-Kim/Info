package com.gunyoung.info.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {
	
	/**
	 * <pre>
	 *  - 기능: 잘못된 접근에 대해 redirect될 화면을 반환하는 컨트롤러
	 *  - 반환:
	 *  	- 성공
	 * 		View: errorpage.html 
	 * <pre>
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/errorpage", method=RequestMethod.GET)
	public ModelAndView error(ModelAndView mav) {
		mav.setViewName("errorpage");
		return mav;
	}
}
