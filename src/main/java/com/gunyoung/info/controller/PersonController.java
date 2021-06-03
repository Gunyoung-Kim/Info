package com.gunyoung.info.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.services.ContentService;
import com.gunyoung.info.services.PersonService;
import com.gunyoung.info.services.SpaceService;

@Controller
public class PersonController {
	
	@Autowired
	PersonService personService;
	
	@Autowired
	SpaceService spaceService;
	
	@Autowired
	ContentService contentService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	/*
	 *  - 기능: 메인 뷰 반환하는 컨트롤러
	 *  - 반환:
	 *  	- 성공
	 *  	View: index.html
	 */
	@RequestMapping(value ="/", method =RequestMethod.GET)
	public String index() {
		return "index";
	}
	
	/*
	 *  - 기능: 로그인 뷰를 반환하는 컨트롤
	 *  - 반환:
	 *  	- 성공
	 *  	View: login.html
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	
	/*
	 *  - 기능: 회원 가입 뷰를 반환하는 컨트롤러
	 *  - 반환:
	 *  	- 성공
	 *  	View: join.html
	 */
	@RequestMapping(value="/join" , method = RequestMethod.GET)
	public ModelAndView join(@ModelAttribute("formModel") Person person, ModelAndView mav) {
		mav.setViewName("join");
		mav.addObject("formModel", person);
		return mav;
	}
	
	/*
	 *  - 기능: 회원 가입 처리를 하는 컨트롤러
	 *  - 반환:
	 *  	- 성공
	 * 		View: join.html 
	 *  	- 실패
	 *  	이미 존재하는 이메일로 들어옴 -> 프론트에서 막았지만 뷰가 아닌 잘못된 경로로 들어올 때 대비
	 *  	formModel이 유효성 검증(검증은 백에서 실행) 실패 
	 */
	@RequestMapping(value="/join", method = RequestMethod.POST)
	public ModelAndView joinPost(@ModelAttribute("formModel") @Valid Person person, ModelAndView mav) {
		if(personService.existsByEmail(person.getEmail())) {
			return new ModelAndView("redirect:/errorpage"); 
		};
		
		person.setPassword(passwordEncoder.encode(person.getPassword()));
		personService.save(person);
		return new ModelAndView("redirect:/");
	}
}
