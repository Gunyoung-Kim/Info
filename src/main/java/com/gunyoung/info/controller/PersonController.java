package com.gunyoung.info.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.info.controller.util.ModelAndPageView;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.dto.JoinDTO;
import com.gunyoung.info.dto.MainListDTO;
import com.gunyoung.info.dto.email.EmailDTO;
import com.gunyoung.info.dto.oauth2.OAuth2Join;
import com.gunyoung.info.error.code.PersonErrorCode;
import com.gunyoung.info.error.exceptions.access.NotMyResourceException;
import com.gunyoung.info.error.exceptions.duplication.PersonDuplicateException;
import com.gunyoung.info.security.UserDetailsVO;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.services.email.EmailService;
import com.gunyoung.info.util.AuthorityUtil;

import lombok.RequiredArgsConstructor;

/**
 * Person 관련 화면 반환하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class PersonController {
	
	public static final int INDEX_VIEW_PAGE_SIZE = 10;
	
	private final PersonService personService;
	
	private final PasswordEncoder passwordEncoder;
	
	private final EmailService emailService;
	
	/**
	 * <pre>
	 *  - 기능: 메인 뷰 반환
	 *  - View: index.html
	 *  </pre>
	 *  @param page 메인 뷰에 보여줄 리스트의 페이지 값 (default=1)
	 *  @author kimgun-yeong
	 */
	@GetMapping(value ="/")
	public ModelAndView indexViewByPage(@RequestParam(value = "page",required = false, defaultValue = "1") Integer page, 
			@RequestParam(value = "keyword", required = false) String keyword, ModelAndPageView mav) {
		if(isUserAuthoritiesContainsRolePre()) {
			return new ModelAndView("redirect:/oauth2/join");
		}
		
		Page<Person> pageResult = getPageResultForIndexView(keyword, page);
		long totalPageNum = getTotalPageNumForIndexView(keyword);
		
		List<MainListDTO> resultList = MainListDTO.of(pageResult);
		
		mav.setPageNumbers(page, totalPageNum);
		mav.addObject("listObject",resultList);
		
		mav.setViewName("index");
		
		return mav;
	}
	
	private boolean isUserAuthoritiesContainsRolePre() {
		Collection<? extends GrantedAuthority> loginUserAuthorities = AuthorityUtil.getSessionUserAuthorities();
		return loginUserAuthorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_PRE"));
	}
	
	private Page<Person> getPageResultForIndexView(String keyword, Integer page) {
		if(keyword == null) {
			return personService.findAllInPage(page);
		}
		return personService.findByNameKeywordInPage(page, keyword);
	}
	
	private long getTotalPageNumForIndexView(String keyword) {
		if(keyword == null) {
			return personService.countAll()/INDEX_VIEW_PAGE_SIZE +1;
		}
		return personService.countWithNameKeyword(keyword)/INDEX_VIEW_PAGE_SIZE +1;
	}
	
	/**
	 * <pre>
	 *  - 기능: 로그인 뷰를 반환
	 *  - View: login.html
	 *  </pre>
	 *  @author kimgun-yeong
	 */
	@GetMapping(value = "/login")
	public String loginView() {
		return "login";
	}
	
	/**
	 * <pre>
	 *  - 기능: 회원 가입 뷰를 반환
	 *  - View: join.html
	 *  </pre>
	 *  @author kimgun-yeong
	 */
	@GetMapping(value="/join")
	public ModelAndView joinView(@ModelAttribute("formModel") JoinDTO joinDTO, ModelAndView mav) {
		mav.addObject("formModel", joinDTO);
		
		mav.setViewName("join");
		return mav;
	}
	
	/**
	 * <pre>
	 *  - 기능: 회원 가입 처리
	 * 	- View: join.html 
	 *  </pre>
	 *  @param joinDTO 회원가입을 위한 Person 객체
	 *  @throws PersonDuplicateException 이미 존재하는 이메일로 회원가입 시도시 발생
	 *  @author kimgun-yeong
	 */
	@PostMapping(value="/join")
	public ModelAndView join(@ModelAttribute("formModel") @Valid JoinDTO joinDTO) {
		if(personService.existsByEmail(joinDTO.getEmail())) {
			throw new PersonDuplicateException(PersonErrorCode.PERSON_DUPLICATION_FOUNDED_ERROR.getDescription());
		}
		encodePasswordAndSavePerson(joinDTO);
		sendEmailForJoin(joinDTO.getEmail());
		
		return new ModelAndView("redirect:/");
	}
	
	private void encodePasswordAndSavePerson(JoinDTO joinDTO) {
		joinDTO.setPassword(passwordEncoder.encode(joinDTO.getPassword()));
		personService.save(joinDTO.createPerson());
	}
	
	/**
	 * <pre>
	 *  - 기능: 소셜로그인한 이메일이 회원가입 되어있지 않았을 때 회원가입하기 위한 페이지 반환 
	 *  - View: joinOAuth.html
	 *  </pre>
	 *  @throws PersonDuplicateException 해당 접속자가 이미 가입되있는 사람일 경우
	 *  @author kimgun-yeong 
	 */
	
	@GetMapping(value= "/oauth2/join") 
	public ModelAndView oAuth2JoinView(@ModelAttribute("formModel") @Valid OAuth2Join formModel, ModelAndView mav) {
		String loginUserEmail = AuthorityUtil.getSessionUserEmail();
		if(personService.existsByEmail(loginUserEmail)) {
			throw new PersonDuplicateException(PersonErrorCode.PERSON_DUPLICATION_FOUNDED_ERROR.getDescription());
		}
		
		formModel.setEmail(loginUserEmail);
		
		mav.addObject("formModel", formModel);
		
		mav.setViewName("joinOAuth");
		
		return mav;
	}
	
	/**
	 * <pre>
	 *  - 기능: 소셜 로그인한 이메일 회원 가입 처리
	 *  - View: redirect -> index.html
	 *  - DB: 해당 person 추가
	 *  </pre>
	 *   @param formModel 소셜 로그인한 주체의 회원가입을 위한 OAuth2Join DTO 객체
	 *   @throws NotMyResourceException 접속한 이메일과 전송된 이메일이 불일치할 때 
	 *   @author kimgun-yeong
	 */	
	
	@PostMapping(value="/oauth2/join") 
	public ModelAndView oAuth2Join(@ModelAttribute("formModel") @Valid OAuth2Join formModel) {
		if(isSessionUserEmailAndEmailInFormMisMatch(formModel.getEmail())) {
			throw new NotMyResourceException(PersonErrorCode.RESOURCE_IS_NOT_MINE_ERROR.getDescription());
		}
		Person person = getNewSavedPersonWithEncodedPassword(formModel);
		setNewAuthenticationInSecurityContext(person);		
		sendEmailForJoin(formModel.getEmail());

		return new ModelAndView("redirect:/");
	}
	
	private boolean isSessionUserEmailAndEmailInFormMisMatch(String emailInFormModel) {
		String userEmail = AuthorityUtil.getSessionUserEmail();
		return !userEmail.equals(emailInFormModel);
	}
	
	private Person getNewSavedPersonWithEncodedPassword(OAuth2Join formModel) {
		String encodedPassword = passwordEncoder.encode(formModel.getPassword());
		Person person = formModel.createPersonFromOAuth2Join(encodedPassword);
		personService.save(person);
		
		return person;
	}
	
	private void setNewAuthenticationInSecurityContext(Person person) {
		List<GrantedAuthority> newAuthorityList = getNewAuthroritiesForRoleUser();
		
		UserDetails newUserDetails = new UserDetailsVO(person.getEmail(),person.getPassword(),person.getRole());
		Authentication newAuth = new UsernamePasswordAuthenticationToken(newUserDetails,null,newAuthorityList);
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}
	
	private List<GrantedAuthority> getNewAuthroritiesForRoleUser() {
		List<GrantedAuthority> newAuthorityList = new ArrayList<>();
		newAuthorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
		return newAuthorityList;
	}

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
