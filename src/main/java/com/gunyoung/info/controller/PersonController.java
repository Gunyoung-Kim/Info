package com.gunyoung.info.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

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
import com.gunyoung.info.dto.email.EmailDTO;
import com.gunyoung.info.dto.oauth2.OAuth2Join;
import com.gunyoung.info.error.code.PersonErrorCode;
import com.gunyoung.info.error.exceptions.access.NotMyResourceException;
import com.gunyoung.info.error.exceptions.duplication.PersonDuplicateException;
import com.gunyoung.info.security.UserDetailsVO;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.services.email.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Person 관련 화면 반환하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
@Setter
@RequiredArgsConstructor
public class PersonController {
	private static final int PAGE_SIZE = 10;
	
	private final PersonService personService;
	
	private final PasswordEncoder passwordEncoder;
	
	private final EmailService emailService;
	
	/**
	 * <pre>
	 *  - 기능: 메인 뷰 반환하는 컨트롤러
	 *  - 반환:
	 *  	- 성공
	 *  	View: index.html
	 *  </pre>
	 *  @param page 메인 뷰에 보여줄 리스트의 페이지 값 (default=1)
	 *  @author kimgun-yeong
	 */
	
	@RequestMapping(value ="/", method =RequestMethod.GET)
	public ModelAndView indexByPage(@RequestParam(value="page",required=false,defaultValue="1") Integer page,@RequestParam(value="keyword",required=false) String keyword, ModelAndView mav) {
		if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PRE"))) {
			return new ModelAndView("redirect:/oauth2/join");
		}
		
		Page<Person> pageResult;
		long totalPageNum;

		if(keyword != null) {
			pageResult = personService.findByNameKeywordInPage(keyword);
			totalPageNum = personService.countWithNameKeyword(keyword)/PAGE_SIZE +1;
		} else {
			pageResult = personService.getAllInPage(page);
			totalPageNum = personService.countAll()/PAGE_SIZE +1;
		}
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
	
	/**
	 * <pre>
	 *  - 기능: 로그인 뷰를 반환하는 컨트롤
	 *  - 반환:
	 *  	- 성공
	 *  	View: login.html
	 *  </pre>
	 *  @author kimgun-yeong
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	
	/**
	 * <pre>
	 *  - 기능: 회원 가입 뷰를 반환하는 컨트롤러
	 *  - 반환:
	 *  	- 성공
	 *  	View: join.html
	 *  </pre>
	 *  @author kimgun-yeong
	 */
	@RequestMapping(value="/join" , method = RequestMethod.GET)
	public ModelAndView join(@ModelAttribute("formModel") Person person, ModelAndView mav) {
		mav.setViewName("join");
		mav.addObject("formModel", person);
		return mav;
	}
	
	/**
	 * <pre>
	 *  - 기능: 회원 가입 처리를 하는 컨트롤러
	 *  - 반환:
	 *  	- 성공
	 * 		View: join.html 
	 *  </pre>
	 *  @param person 회원가입을 위한 Person 객체
	 *  @throws PersonDuplicateException 이미 존재하는 이메일로 회원가입 시도시 발생
	 *  @author kimgun-yeong
	 */
	@RequestMapping(value="/join", method = RequestMethod.POST)
	public ModelAndView joinPost(@ModelAttribute("formModel") @Valid Person person, ModelAndView mav) {
		if(personService.existsByEmail(person.getEmail())) {
			throw new PersonDuplicateException(PersonErrorCode.PERSON_DUPLICATION_FOUNDED_ERROR.getDescription());
		};
		
		person.setPassword(passwordEncoder.encode(person.getPassword()));
		personService.save(person);
		
		 sendEmailForJoin(person.getEmail());
		return new ModelAndView("redirect:/");
	}
	
	/**
	 * <pre>
	 *  - 기능: 소셜로그인한 이메일이 회원가입 되어있지 않았을 때 회원가입하기 위한 페이지 반 
	 *  - 반환:
	 *  	- 성공: 
	 *  	View: joinOAuth.html
	 *  </pre>
	 *  @throws PersonDuplicateException 해당 접속자가 이미 가입되있는 사람일 경우
	 *  @author kimgun-yeong 
	 */
	
	@RequestMapping(value= "/oauth2/join" , method = RequestMethod.GET) 
	public ModelAndView oAuth2Join(@ModelAttribute("formModel") @Valid OAuth2Join formModel, ModelAndView mav) {
		String connectedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if(personService.existsByEmail(connectedEmail)) {
			throw new PersonDuplicateException(PersonErrorCode.PERSON_DUPLICATION_FOUNDED_ERROR.getDescription());
		}
		
		mav.setViewName("joinOAuth");
		
		formModel.setEmail(connectedEmail);
		
		mav.addObject("formModel", formModel);
		
		return mav;
	}
	
	/**
	 * <pre>
	 *  - 기능: 소셜 로그인한 이메일 회원 가입 처리하는 컨트롤러
	 *  - 반환:
	 *  	 - 성공:
	 *  	View: redirect -> index.html
	 *   	DB: 해당 person 추가
	 *  </pre>
	 *   @param formModel 소셜 로그인한 주체의 회원가입을 위한 OAuth2Join DTO 객체
	 *   @throws NotMyResourceException 접속한 이메일과 전송된 이메일이 불일치할 때 
	 *   @author kimgun-yeong
	 */	
	
	@RequestMapping(value="/oauth2/join", method = RequestMethod.POST) 
	public ModelAndView oAuth2JoinPost(@ModelAttribute("formModel") @Valid OAuth2Join formModel) {
		String connectEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if(!connectEmail.equals(formModel.getEmail())) {
			throw new NotMyResourceException(PersonErrorCode.RESOURCE_IS_NOT_MINE_ERROR.getDescription());
		}
		
		Person person = new Person();
		person.setEmail(formModel.getEmail());
		person.setPassword(passwordEncoder.encode(formModel.getPassword()));
		person.setFirstName(formModel.getFirstName());
		person.setLastName(formModel.getLastName());
		
		personService.save(person);
		
		List<GrantedAuthority> newAuthorityList = new ArrayList<>();
		
		newAuthorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		Authentication newAuth = new UsernamePasswordAuthenticationToken(new UserDetailsVO(person.getEmail(),person.getPassword(),person.getRole()),null,newAuthorityList);
		
		SecurityContextHolder.getContext().setAuthentication(newAuth);
		
		 sendEmailForJoin(formModel.getEmail());
		
		return new ModelAndView("redirect:/");
	}
	
	/**
	 * 회원 가입 후 축하 이메일 전송을 위한 메소드 <br>
	 * 회원 가입 메소드 성공 이후 실행됨 
	 * 
	 * @param receiveMail mail을 받을 주체의 email 주소
	 * @author kimgun-yeong
	 */
	private void sendEmailForJoin(String receiveMail) {
		EmailDTO email = EmailDTO.builder()
								 .senderMail("gun025bba@google.com")
								 .senderName("INFO")
								 .receiveMail(receiveMail)
								 .subject("INFO 가입을 환영합니다.")
								 .message("INFO 가입을 굉장히 환영합니다.")
								 .build();
		
		emailService.sendEmail(email);					
	}
}
