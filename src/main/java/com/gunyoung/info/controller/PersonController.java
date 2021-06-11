package com.gunyoung.info.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.dto.MainListObject;
import com.gunyoung.info.dto.OAuth2Join;
import com.gunyoung.info.security.UserDetailsVO;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.services.domain.SpaceService;

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
	
	private static final int PAGE_SIZE = 5;
	
	@RequestMapping(value ="/", method =RequestMethod.GET)
	public ModelAndView indexByPage(@RequestParam(value="page",required=false,defaultValue="1") Integer page, ModelAndView mav) {
		if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PRE"))) {
			return new ModelAndView("redirect:/oauth2/join");
		}
		
		Page<Person> pageResult = personService.getAllInPage(page);
		long totalPageNum = personService.countAll()/PAGE_SIZE +1;
		
		List<MainListObject> resultList = new LinkedList<>();
		
		for(Person p : pageResult) {
			resultList.add(new MainListObject(p.getFullName(),p.getEmail()));
		}
		
		mav.addObject("listObject",resultList);
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/PAGE_SIZE)*PAGE_SIZE+1);
		mav.addObject("lastIndex",(page/PAGE_SIZE)*PAGE_SIZE+PAGE_SIZE-1 > totalPageNum ? totalPageNum : (page/PAGE_SIZE)*PAGE_SIZE+PAGE_SIZE-1);
		mav.setViewName("index");
		
		return mav;
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
	
	/*
	 *  - 기능: 소셜로그인한 이메일이 회원가입 되어있지 않았을 때 회원가입하기 위한 페이지 반 
	 *  - 반환:
	 *  	- 성공: 
	 *  	View: joinOAuth.html
	 *  	- 실패: 
	 */
	
	@RequestMapping(value= "/oauth2/join" , method = RequestMethod.GET) 
	public ModelAndView oAuth2Join(@ModelAttribute("formModel") @Valid OAuth2Join formModel, ModelAndView mav) {
		mav.setViewName("joinOAuth");
		
		formModel.setEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		
		mav.addObject("formModel", formModel);
		
		return mav;
	}
	
	/*
	 *  - 기능: 소셜 로그인한 이메일 회원 가입 처리하는 컨트롤러
	 *  - 반환:
	 *  	 - 성공:
	 *  	View: redirect -> index.html
	 *   	DB: 해당 person 추가
	 *   	- 실패
	 */
	
	@RequestMapping(value="/oauth2/join", method = RequestMethod.POST) 
	public ModelAndView oAuth2JoinPost(@ModelAttribute("formModel") @Valid OAuth2Join formModel) {
		
		Person person = new Person();
		person.setEmail(formModel.getEmail());
		person.setPassword(passwordEncoder.encode(formModel.getPassword()));
		person.setFirstName(formModel.getFirstName());
		person.setLastName(formModel.getLastName());
		
		personService.save(person);
		
		List<GrantedAuthority> newAuthorityList = new ArrayList<>();
		
		newAuthorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		Authentication newAuth = new UsernamePasswordAuthenticationToken(new UserDetailsVO(person.getEmail(),person.getPassword()),null,newAuthorityList);
		
		SecurityContextHolder.getContext().setAuthentication(newAuth);
		
		return new ModelAndView("redirect:/");
	}
	
	/*
	 *  - 기능: 회원 탈퇴를 처리하는 컨트롤러
	 *  - 반환:
	 *  	- 성공
	 *  	View: index.html
	 *  	DB: 해당 person 삭제
	 *  	- 실패
	 *  	해당 계정이 DB에 존재하지 않을 때
	 *  	로그인 계정이 탈퇴 계정과 일치하지 않을 때
	 */
	
	@RequestMapping(value="/withdraw", method=RequestMethod.DELETE)
	public ModelAndView personWithdraw(@RequestParam("email") String email,ModelAndView mav) {
		
		if(!personService.existsByEmail(email)) {
			return new ModelAndView("redirect:/errorpage");
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		
		if(!auth.getName().equals(email)) {
			return new ModelAndView("redirect:/errorpage");
		}
		
		personService.deletePerson(personService.findByEmail(email));
		
		return new ModelAndView("redirect:/logout");
	}
}
