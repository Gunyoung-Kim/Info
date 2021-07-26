package com.gunyoung.info.controller;

import java.util.List;

import javax.validation.Valid;

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
import com.gunyoung.info.error.code.PersonErrorCode;
import com.gunyoung.info.error.exceptions.nonexist.PersonNotFoundedException;
import com.gunyoung.info.services.domain.PersonService;

import lombok.RequiredArgsConstructor;

/**
 * Space 도메인 관련 처리를 담당하는 컨트롤러 클래스 
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class SpaceController {
	
	private final PersonService personService;
	
	/**
	 * <pre>
	 *  - 기능: 현재 로그인되있는 사용자 본인의 포트폴리오 페이지 반환
	 *  - 반환: 
	 *  	- 성공
	 *  	View: portfolio.html (현재 로그인 유저의 포트폴리오), login.html(로그인 안되있으면)
	 *  	- 실패 
	 *  </pre>
	 *  @author kimgun-yeong
	 */
	@RequestMapping(value="/space", method= RequestMethod.GET)
	public ModelAndView myspace(ModelAndView mav) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return new ModelAndView("redirect:/space/"+auth.getName());
	}
	
	/**
	 * <pre>
	 *  - 기능: 개인 포트폴리오 페이지 반환, url에 포트폴리오 주인 이메일
	 *  - 반환:
	 *  	- 성공
	 *  	View: portfolio.html (url에 입력된 이메일 유저의 포트폴리오)
	 *  	Model: profile -> ProfileObject (포트폴리오 주인의 프로필 정보를 전달하는 DTO- Person+Space 일부 필드)
	 *  		   contents -> List<Content> (포트폴리오에 있는 프로젝트 리스트)
	 *  		   isHost -> boolean (현재 로그인된 유저가 해당 포트폴리오의 주인인지 여부-> 템플릿에 변화 주기위함(ex. 프로젝트 수정 버튼 추가))
	 *  </pre>
	 *  @param email 열람하려는 포트폴리오 주인의 email 값
	 *  @throws PersonNotFoundedException url에 입력된 email이 DB에 없으면 실패 페이지 반환
	 *  @author kimgun-yeong
	 */
	@RequestMapping(value="/space/{email}", method= RequestMethod.GET)
	public ModelAndView space(@PathVariable String email, ModelAndView mav) {
		Person person = personService.findByEmailWithSpace(email);
		
		if(person == null) {
			throw new PersonNotFoundedException(PersonErrorCode.PERSON_NOT_FOUNDED_ERROR.getDescription());
		}
		
		Space space = person.getSpace();
		ProfileObject profile = new ProfileObject();
		profile.settingByPersonAndSpace(person, space);
		mav.addObject("profile", profile);
		
		List<Content> contents = space.getContents();
		mav.addObject("contents",contents);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		mav.addObject("isHost", email.equals(auth.getName()));
		
		mav.setViewName("portfolio");
		
		return mav;
	}
	
	/** 
	 * <pre>
	 *  - 기능: 현재 로그인한 유저의 프로필을 변경하기 위한 뷰를 반환하는 컨트롤러
	 *  - 반환: 
	 *  	- 성공
	 *  	View: updateProfile.html (프로필 업데이트 사항 작성을 위한 템플릿)
	 *  	Model: formModel->ProfileObject(프로필 업데이트 사항 전달을 위한 DTO객체, Person+ Space 일부필드)
	 *  </pre>
	 *  @throws PersonNotFoundedException 현재 로그인된 유저의 이메일이 DB에 없으면
	 *  @author kimgun-yeong
	 */
	@RequestMapping(value="/space/updateprofile", method = RequestMethod.GET)
	public ModelAndView updateProfile(@ModelAttribute("formModel") ProfileObject profileObject, ModelAndView mav) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		
		Person person = personService.findByEmailWithSpace(email);
		
		if(person == null) {
			throw new PersonNotFoundedException(PersonErrorCode.PERSON_NOT_FOUNDED_ERROR.getDescription());
		}
		
		Space space = person.getSpace();
		
		profileObject.settingByPersonAndSpace(person, space);
		mav.addObject("formModel", profileObject);
		
		mav.setViewName("updateProfile");
		
		return mav;
	}
	
	/**
	 * <pre>
	 *  - 기능: updateProfile이 반환한 뷰에서 작성한 프로필 변경 사항들을 유저가 POST Request로 보내면 이를 처리하기 위한 컨트롤러
	 * 	- 반환:
	 * 		- 성공
	 * 		View: updateProfile.html (입력된 프로필 정보로 다시 전송)
	 * 		DB: ProfileObject에서 Person 및 Space의 변경 사항 추출 후 save
	 * </pre>
	 * @param profileObject Person의 필드 및 Space의 필드 값 수정을 위한 ProfileObject DTO 객체 
	 * @throws PersonNotFoundedException 전달된 ProfileObject에 있는 이메일이 DB에 존재하지 않을때 
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/space/updateprofile", method = RequestMethod.POST)
	public ModelAndView updateProfilePost(@ModelAttribute("formModel") @Valid ProfileObject profileObject, ModelAndView mav) {
		Person person = personService.findByEmailWithSpace(profileObject.getEmail());
		if(person == null) {
			throw new PersonNotFoundedException(PersonErrorCode.PERSON_NOT_FOUNDED_ERROR.getDescription());
		}
		
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
