package com.gunyoung.info.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.services.PersonService;
import com.gunyoung.info.services.SpaceService;

@Controller
public class ViewController {
	
	@Autowired
	PersonService personService;
	
	@Autowired
	SpaceService spaceService;
	
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
			personService.save(person);
			res = new ModelAndView("redirect:/");
		} else {
			mav.setViewName("join");
			res = mav;
		}
		return res;
	}
	
	@RequestMapping(value="/space/{email}")
	public String space(@PathVariable String email) {
		return "";
	}
}
