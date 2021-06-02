package com.gunyoung.info.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	 */
	@RequestMapping(value="/join", method = RequestMethod.POST)
	public ModelAndView joinPost(@Valid @ModelAttribute("formModel") Person person,BindingResult result, ModelAndView mav) {
		ModelAndView res = null;
		if(!result.hasErrors()) {
			if(personService.existsByEmail(person.getEmail())) {
				return new ModelAndView("redirect:/errorpage"); // 여기 오는 경우는 회원가입 폼이 아닌 잘못된 방식
			};
			person.setPassword(passwordEncoder.encode(person.getPassword()));
			personService.save(person);
			res = new ModelAndView("redirect:/");
		} else {
			mav.setViewName("join");
			res = mav;
		}
		return res;
	}
}
