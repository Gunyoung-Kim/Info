package com.gunyoung.info.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.services.PersonService;
import com.gunyoung.info.services.SpaceService;

@Controller
public class ViewController {
	
	@Autowired
	PersonService personService;
	
	@Autowired
	SpaceService spaceService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@RequestMapping(value ="/", method =RequestMethod.GET)
	public String index() {
		return "index";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String loginToJoinPage( @RequestParam(value="action", required=true) String action) {
		if(action.equals("join")) {
			return "redirect:/join";
		}
		else 
			return "login";
	}

	
	@RequestMapping(value="/join" , method = RequestMethod.GET)
	public ModelAndView join(@ModelAttribute("formModel") Person person, ModelAndView mav) {
		mav.setViewName("join");
		mav.addObject("formModel", person);
		return mav;
	}
	
	@RequestMapping(value="/join", method = RequestMethod.POST)
	public ModelAndView join_post(@Valid @ModelAttribute("formModel") Person person,BindingResult result, ModelAndView mav) {
		ModelAndView res = null;
		if(!result.hasErrors()) {
			person.setPassword(passwordEncoder.encode(person.getPassword())); //-> password validation 문제로 나중에 넣자
			personService.save(person);
			res = new ModelAndView("redirect:/");
		} else {
			mav.setViewName("join");
			res = mav;
		}
		return res;
	}
	
	@RequestMapping(value="/space/{email}")
	public ModelAndView space(@PathVariable String email, ModelAndView mav) {
		mav.setViewName("portfolio");
		Person person = personService.findByEmail(email);
		if(person == null) {
			// failed page
		}
		mav.addObject("person", person);
		
		Space space = person.getSpace();
		List<Content> contents = space.getContents();
		mav.addObject("contents",contents);
		
		return mav;
	}
}
