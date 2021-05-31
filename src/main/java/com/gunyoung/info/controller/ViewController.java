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
import com.gunyoung.info.dto.ProfileObject;
import com.gunyoung.info.services.ContentService;
import com.gunyoung.info.services.PersonService;
import com.gunyoung.info.services.SpaceService;

@Controller
public class ViewController {
	
	@Autowired
	PersonService personService;
	
	@Autowired
	SpaceService spaceService;
	
	@Autowired
	ContentService contentService;
	
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
	public ModelAndView joinPost(@Valid @ModelAttribute("formModel") Person person,BindingResult result, ModelAndView mav) {
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
		
		Space space = person.getSpace();
		ProfileObject profile = new ProfileObject();
		profile.settingByPersonAndSpace(person, space);
		mav.addObject("profile", profile);
		List<Content> contents = space.getContents();
		mav.addObject("contents",contents);
		
		return mav;
	}
	
	@RequestMapping(value="/space/makecontent/{email}", method = RequestMethod.GET)
	public ModelAndView createContent(@PathVariable String email,@ModelAttribute("formModel") Content content, ModelAndView mav) {
		mav.setViewName("createContent");
		Person person = personService.findByEmail(email);
		if(person == null) {
			// failed page
		}
		mav.addObject("person",person);
		mav.addObject("formModel", content);
		
		return mav;
	}
	
	@RequestMapping(value="/space/makecontent/{email}", method = RequestMethod.POST)
	public ModelAndView createContentPost(@PathVariable String email,@Valid @ModelAttribute("formModel") Content content ,ModelAndView mav) {
		Person person = personService.findByEmail(email);
		if(person == null) {
			// failed page
		}
		Space space = person.getSpace();
		content.setSpace(space);
		contentService.save(content);
		
		space.getContents().add(content); // 이거 없으면 어떻게 되나?
		
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value="/space/updateprofile/{email}", method = RequestMethod.GET)
	public ModelAndView updateProfile(@PathVariable String email, @ModelAttribute("formModel") ProfileObject profileObject, ModelAndView mav) {
		Person person = personService.findByEmail(email);
		if(person == null) {
			
		}
		
		mav.setViewName("updateProfile");
		Space space = person.getSpace();
		
		profileObject.settingByPersonAndSpace(person, space);
		
		mav.addObject("formModel", profileObject);
		
		return mav;
	}
	
	@RequestMapping(value="/space/updateprofile/{email}", method = RequestMethod.POST)
	public ModelAndView updateProfilePost(@PathVariable String email, @ModelAttribute("formModel") ProfileObject profileObject, ModelAndView mav) {
		Person person = personService.findByEmail(email);
		if(person == null) {
			
		}
		
		mav.setViewName("updateProfile");
		
		person.setFirstName(profileObject.getFirstName());
		person.setLastName(profileObject.getLastName());
		personService.save(person);
		
		Space space = person.getSpace();
		space.setDescription(profileObject.getDescription());
		space.setGithub(profileObject.getGithub());
		space.setFacebook(profileObject.getFacebook());
		space.setInstagram(profileObject.getInstagram());
		space.setTweeter(profileObject.getTweeter());
		spaceService.save(space);
		
		return updateProfile(email,profileObject, mav);
	}
}
