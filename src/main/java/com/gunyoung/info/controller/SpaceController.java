package com.gunyoung.info.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.dto.ProfileObject;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.services.domain.SpaceService;

@Controller
public class SpaceController {
	@Autowired
	PersonService personService;
	
	@Autowired
	SpaceService spaceService;
	
	@Autowired
	ContentService contentService;
	
	/*
	 *  - 기능: 현재 로그인되있는 사용자 본인의 포트폴리오 페이지 반환
	 *  - 반환: 
	 *  	- 성공
	 *  	View: portfolio.html (현재 로그인 유저의 포트폴리오), login.html(로그인 안되있으면)
	 *  	- 실패 
	 */
	@RequestMapping(value="/space", method= RequestMethod.GET)
	public ModelAndView myspace(ModelAndView mav) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return new ModelAndView("redirect:/space/"+auth.getName());
	}
	
	/*
	 *  - 기능: 개인 포트폴리오 페이지 반환, url에 포트폴리오 주인 이메일
	 *  - 반환:
	 *  	- 성공
	 *  	View: portfolio.html (url에 입력된 이메일 유저의 포트폴리오)
	 *  	Model: profile -> ProfileObject (포트폴리오 주인의 프로필 정보를 전달하는 DTO- Person+Space 일부 필드)
	 *  		   contents -> List<Content> (포트폴리오에 있는 프로젝트 리스트)
	 *  		   isHost -> boolean (현재 로그인된 유저가 해당 포트폴리오의 주인인지 여부-> 템플릿에 변화 주기위함(ex. 프로젝트 수정 버튼 추가))
	 *  	- 실패
	 *  	url에 입력된 email이 DB에 없으면 실패 페이지 반환
	 */
	@RequestMapping(value="/space/{email}", method= RequestMethod.GET)
	public ModelAndView space(@PathVariable String email, ModelAndView mav) {
		if(!personService.existsByEmail(email)) {
			return new ModelAndView("redirect:/errorpage");
		}
		
		Person person = personService.findByEmail(email);
		mav.setViewName("portfolio");
		
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
	
	/*
	 *  - 기능: 현재 로그인한 유저의 프로필을 변경하기 위한 뷰를 반환하는 컨트롤러
	 *  - 반환: 
	 *  	- 성공
	 *  	View: updateProfile.html (프로필 업데이트 사항 작성을 위한 템플릿)
	 *  	Model: formModel->ProfileObject(프로필 업데이트 사항 전달을 위한 DTO객체, Person+ Space 일부필드)
	 *  	- 실패
	 *  	현재 로그인된 유저의 이메일이 DB에 없으면 실패 페이지 반환 -> 일어나지 않을 확률 100에 수렴
	 */
	@RequestMapping(value="/space/updateprofile", method = RequestMethod.GET)
	public ModelAndView updateProfile(@ModelAttribute("formModel") ProfileObject profileObject, ModelAndView mav) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		
		if(!personService.existsByEmail(email)) {
			return new ModelAndView("redirect:/errorpage");
		}
		
		Person person = personService.findByEmail(email);
		
		mav.setViewName("updateProfile");
		Space space = person.getSpace();
		
		profileObject.settingByPersonAndSpace(person, space);
		
		mav.addObject("formModel", profileObject);
		
		return mav;
	}
	
	/*
	 *  - 기능: updateProfile이 반환한 뷰에서 작성한 프로필 변경 사항들을 유저가 POST Request로 보내면 이를 처리하기 위한 컨트롤러
	 * 	- 반환:
	 * 		- 성공
	 * 		View: updateProfile.html (입력된 프로필 정보로 다시 전송)
	 * 		DB: ProfileObject에서 Person 및 Space의 변경 사항 추출 후 save
	 * 		- 실패
	 * 		ProfileObject의 유효성 불통과 
	 * 	    전달된 ProfileObject에 있는 이메일이 DB에 존재하지 않을때 실패페이지 반환 -> 템플릿에서는 막음
	 */
	@RequestMapping(value="/space/updateprofile", method = RequestMethod.POST)
	public ModelAndView updateProfilePost(@ModelAttribute("formModel") @Valid ProfileObject profileObject, ModelAndView mav) {
		if(!personService.existsByEmail(profileObject.getEmail())) {
			return new ModelAndView("redirect:/errorpage");
		}
		
		Person person = personService.findByEmail(profileObject.getEmail());
		
		mav.setViewName("updateProfile");
		
		person.setFirstName(profileObject.getFirstName());
		person.setLastName(profileObject.getLastName());
		
		Space space = person.getSpace();
		space.setDescription(profileObject.getDescription());
		space.setGithub(profileObject.getGithub());
		space.setFacebook(profileObject.getFacebook());
		space.setInstagram(profileObject.getInstagram());
		space.setTweeter(profileObject.getTweeter());
		
		personService.save(person);
		
		return new ModelAndView("redirect:/space/updateprofile");
	}
}
