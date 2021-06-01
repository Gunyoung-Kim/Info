package com.gunyoung.info.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.gunyoung.info.dto.ContentDTO;
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
			person.setPassword(passwordEncoder.encode(person.getPassword()));
			personService.save(person);
			res = new ModelAndView("redirect:/");
		} else {
			mav.setViewName("join");
			res = mav;
		}
		return res;
	}
	
	@RequestMapping(value="/space", method= RequestMethod.GET)
	public ModelAndView myspace(ModelAndView mav) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return space(auth.getName(), mav);
	}
	
	@RequestMapping(value="/space/{email}", method= RequestMethod.GET)
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
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		mav.addObject("isHost", email.equals(auth.getName()));
		
		return mav;
	}
	
	
	@RequestMapping(value="/space/makecontent/{email}", method = RequestMethod.GET)
	public ModelAndView createContent(@PathVariable String email,@ModelAttribute("formModel") Content content, ModelAndView mav) {
		// 해당 스페이스가 현재 접속자의 것인지 확인하는 작업
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(!email.equals(auth.getName())) {
			return new ModelAndView("redirect:/");   // 접속자가 해당 컨텐츠의 주인이 아니라면 홈으로 보내버림 -> 나중에 전용 에러 페이지 만들자
		}
		
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
		// 해당 스페이스가 현재 접속자의 것인지 확인하는 작업
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(!email.equals(auth.getName())) {
			return new ModelAndView("redirect:/");   // 접속자가 해당 컨텐츠의 주인이 아니라면 홈으로 보내버림 -> 나중에 전용 에러 페이지 만들자
		}
		
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
	
	@RequestMapping(value="/space/updatecontent/{id}", method= RequestMethod.GET)
	public ModelAndView updateContent(@PathVariable long id, @ModelAttribute("formModel") ContentDTO contentDto, ModelAndView mav) {
		Content content = contentService.findById(id);
		if(content == null) {
			// 잘못된 URL 접근으로 해당 id의 Content가 없을
		}
		
		// 해당 컨텐트가 현재 접속자의 것인지 확인하는 작업
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String contentUserEmail = content.getSpace().getPerson().getEmail();
		
		if(!contentUserEmail.equals(auth.getName())) {
			return new ModelAndView("redirect:/");   // 접속자가 해당 컨텐츠의 주인이 아니라면 홈으로 보내버림 -> 나중에 전용 에러 페이지 만들자
		}
		
		contentDto.settingByEmailAndContent(contentUserEmail, content);
		mav.setViewName("updateContent");
		mav.addObject("formModel", contentDto);
		
		return mav;
	}
	
	@RequestMapping(value="/space/updatecontent/{id}", method= RequestMethod.POST) 
	public ModelAndView updateContentPost(@PathVariable long id, @ModelAttribute("formModel") ContentDTO contentDto, ModelAndView mav) {
		Content content = contentService.findById(id);
		if(content == null) {
			// 잘못된 URL 접근으로 해당 id의 Content가 없을
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String hostEmail = contentDto.getHostEmail();
		if(!hostEmail.equals(auth.getName())) {
			// 리퀘스트 보낸 사람이 이 콘텐츠의 주인과 다를때 -> 나중에 전용 에러 페이지 만들자
			return new ModelAndView("redirect:/"); 
		}
		
		content.setTitle(contentDto.getTitle());
		content.setDescription(contentDto.getDescription());
		content.setContributors(contentDto.getContributors());
		content.setSkillstacks(contentDto.getSkillstacks());
		content.setStartedAt(contentDto.getStartedAt());
		content.setEndAt(contentDto.getEndAt());
		content.setContents(contentDto.getContents());
		content.setLinks(contentDto.getLinks());
		
		contentService.save(content);
		
		return new ModelAndView("redirect:/space");
	}
	
	@RequestMapping(value="/space/deletecontent/{id}", method = RequestMethod.POST)
	public ModelAndView deleteContent(@PathVariable long id) {
		contentService.deleteContentById(id);
		return new ModelAndView("redirect:/");
	}
	
	
	@RequestMapping(value="/space/updateprofile", method = RequestMethod.GET)
	public ModelAndView updateProfile(@ModelAttribute("formModel") ProfileObject profileObject, ModelAndView mav) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		Person person = personService.findByEmail(email);
		if(person == null) {
			
		}
		
		mav.setViewName("updateProfile");
		Space space = person.getSpace();
		
		profileObject.settingByPersonAndSpace(person, space);
		
		mav.addObject("formModel", profileObject);
		
		return mav;
	}
	
	@RequestMapping(value="/space/updateprofile", method = RequestMethod.POST)
	public ModelAndView updateProfilePost(@ModelAttribute("formModel") ProfileObject profileObject, ModelAndView mav) {
		Person person = personService.findByEmail(profileObject.getEmail());
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
		
		return updateProfile(profileObject, mav);
	}
}
