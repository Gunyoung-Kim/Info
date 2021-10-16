package com.gunyoung.info.controller.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.info.controller.PersonController;
import com.gunyoung.info.controller.util.ModelAndPageView;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.dto.oauth2.OAuth2Join;
import com.gunyoung.info.error.exceptions.access.NotMyResourceException;
import com.gunyoung.info.error.exceptions.duplication.PersonDuplicateException;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.services.email.EmailService;
import com.gunyoung.info.util.PersonTest;

/**
 * {@link PersonController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) PersonController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class PersonControllerUnitTest {
	
	@Mock
	PersonService personService;
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	@Mock
	EmailService emailService;
	
	@InjectMocks
	PersonController personController;
	
	private Integer defalutPageNum = 1;
	
	private ModelAndView mav;
	
	private ModelAndPageView mapv;
	
	private SecurityContext securityContext;
	
	private Authentication auth;
	
	@BeforeEach
	void setup() {
		mav = mock(ModelAndView.class);
		mapv = mock(ModelAndPageView.class);
		
		securityContext = mock(SecurityContext.class);
		SecurityContextHolder.setContext(securityContext);
		
		auth = mock(Authentication.class);
	}
	
	/*
	 * ModelAndView indexViewByPage(@RequestParam(value="page",required=false,defaultValue="1") Integer page, 
			@RequestParam(value="keyword",required=false) String keyword, ModelAndPageView mav)
	 */
	
	@Test
	@DisplayName("메인 뷰 반환 -> OAuth2를 통해 접속한 PRE 유저")
	void indexViewByPageTestPRE() {
		//Given
		mockingAuthorityutilGetSessionUserAuthorities("ROLE_PRE");
		
		//When
		ModelAndView result = personController.indexViewByPage(defalutPageNum, null, mapv);
		
		//Then
		assertEquals("redirect:/oauth2/join", result.getViewName());
	}
	
	@Test
	@DisplayName("메인 뷰 반환 -> 정상, 키워드 없음, PersonService check")
	void indexViewByPageTestNoKeywordCheckPersonService() {	
		//Given
		mockingAuthorityutilGetSessionUserAuthorities("ROLE_USER");
		
		Page<Person> pageResult = new PageImpl<>(new ArrayList<>());
		given(personService.findAllInPage(defalutPageNum)).willReturn(pageResult);
		
		//When
		personController.indexViewByPage(defalutPageNum, null, mapv);
		
		//Then
		then(personService).should(times(1)).countAll();
	}
	
	@Test
	@DisplayName("메인 뷰 반환 -> 정상, 키워드 있음, PersonService check")
	void indexViewByPageTestYesKeywordCheckPersonService() {
		//Given
		mockingAuthorityutilGetSessionUserAuthorities("ROLE_USER");
		
		String keyword = "keyword";
		Page<Person> pageResult = new PageImpl<>(new ArrayList<>());
		given(personService.findByNameKeywordInPage(defalutPageNum, keyword)).willReturn(pageResult);
		
		//When
		personController.indexViewByPage(defalutPageNum, keyword, mapv);
		
		//Then
		then(personService).should(times(1)).countWithNameKeyword(keyword);
	}
	
	@Test
	@DisplayName("메인 뷰 반환 -> 정상, 키워드 없음, ModelAndPageView check")
	void indexViewByPageTestNoKeywordCheckMav() {
		//Given
		mockingAuthorityutilGetSessionUserAuthorities("ROLE_USER");
		
		Page<Person> pageResult = new PageImpl<>(new ArrayList<>());
		given(personService.findAllInPage(defalutPageNum)).willReturn(pageResult);
		
		Long givenPersonNum = Long.valueOf(52);
		given(personService.countAll()).willReturn(givenPersonNum);
		
		//When
		personController.indexViewByPage(defalutPageNum, null, mapv);
		
		//Then
		verifyModelAndView_indexViewByPageTestNoKeywordCheckMav(givenPersonNum);
	}
	
	private void mockingAuthorityutilGetSessionUserAuthorities(String authroity) {
		Collection<? extends GrantedAuthority> authrorities = AuthorityUtils.createAuthorityList(authroity);
		given(securityContext.getAuthentication()).willReturn(auth);
		doReturn(authrorities).when(auth).getAuthorities();
	}
	
	private void verifyModelAndView_indexViewByPageTestNoKeywordCheckMav(Long givenPersonNum) {
		then(mapv).should(times(1)).setPageNumbers(defalutPageNum, givenPersonNum / PersonController.INDEX_VIEW_PAGE_SIZE +1);
		then(mapv).should(times(1)).setViewName("index");
	}
	
	/*
	 * String loginView()
	 */
	
	@Test
	@DisplayName("로그인 뷰를 반환 -> 정상")
	void loginViewTest() {
		//Given
		
		//When
		String result = personController.loginView();
		
		//Then
		assertEquals("login", result);
	}
	
	/*
	 * ModelAndView joinView(@ModelAttribute("formModel") Person person, ModelAndView mav)
	 */
	
	@Test
	@DisplayName("회원 가입 뷰를 반환 -> 정상")
	void joinViewTestCheckMav() {
		//Given
		Person person = PersonTest.getPersonInstance();
		
		//When
		personController.joinView(person, mav);
		
		//Then
		then(mav).should(times(1)).addObject("formModel", person);
		then(mav).should(times(1)).setViewName("join");
	}
	
	/*
	 * ModelAndView join(@ModelAttribute("formModel") @Valid Person person, ModelAndView mav)
	 */
	
	@Test
	@DisplayName("회원 가입 처리 -> 이미 존재하는 이메일로 회원가입 시도")
	void joinTestEmailDuplicated() {
		//Given 
		String exsitEmail = "exist@test.com";
		mockingPersonServiceExistByEmail(exsitEmail, true);
		
		Person person = PersonTest.getPersonInstance(exsitEmail);
		//When, Then
		assertThrows(PersonDuplicateException.class, () -> {
			personController.join(person);
		});
	}
	
	@Test
	@DisplayName("회원 가입 처리 -> 정상, check Password Encode")
	void joinTestCheckPasswordEncoder() {
		//Given
		String personEmail = "test@test.com";
		mockingPersonServiceExistByEmail(personEmail, false);
		Person person = PersonTest.getPersonInstance(personEmail);
		
		String encodedPassword = "245trgd4635";
		given(passwordEncoder.encode(person.getPassword())).willReturn(encodedPassword);
		
		//When
		personController.join(person);
		
		//Then
		assertEquals(encodedPassword, person.getPassword());
	}
	
	@Test
	@DisplayName("회원 가입 처리 -> 정상, check personService save")
	void joinTestCheckPersonServiceSave() {
		//Given
		String personEmail = "test@test.com";
		mockingPersonServiceExistByEmail(personEmail, false);
		Person person = PersonTest.getPersonInstance(personEmail);
		
		//When
		personController.join(person);
		
		//Then
		then(personService).should(times(1)).save(person);
	}
	
	@Test
	@DisplayName("회원 가입 처리 -> 정상, redirect check")
	void joinTestCheckRedirectedURL() {
		//Given
		String personEmail = "test@test.com";
		mockingPersonServiceExistByEmail(personEmail, false);
		Person person = PersonTest.getPersonInstance(personEmail);
		
		//When
		ModelAndView result = personController.join(person);
		
		//Then
		assertEquals("redirect:/", result.getViewName());
	}
	
	private void mockingPersonServiceExistByEmail(String personEmail, boolean result) {
		given(personService.existsByEmail(personEmail)).willReturn(result);
	}
	
	/*
	 * ModelAndView oAuth2JoinView(@ModelAttribute("formModel") @Valid OAuth2Join formModel, ModelAndView mav)
	 */
	
	@Test
	@DisplayName("소셜로그인한 이메일이 회원가입 되어있지 않았을 때 회원가입하기 위한 페이지 반환 -> 해당 접속자가 이미 가입")
	void oAuth2JoinViewTestPersonDuplicated() {
		//Given
		String existEmail = "exist@test.com";
		mockingAuthorityUtilGetSessionUserEmail(existEmail);
		given(personService.existsByEmail(existEmail)).willReturn(true);
		
		OAuth2Join oAuth2Join = PersonTest.getOAuth2JoinInstance(); 
		//When, Then
		assertThrows(PersonDuplicateException.class, () -> {
			personController.oAuth2JoinView(oAuth2Join, mav);
		});
	}
	
	@Test
	@DisplayName("소셜로그인한 이메일이 회원가입 되어있지 않았을 때 회원가입하기 위한 페이지 반환 -> 정상, ModelAndView check")
	void oAuth2JoinViewTestCheckMav() {
		//Given
		String loginPersonEmail = "test@test.com";
		mockingAuthorityUtilGetSessionUserEmail(loginPersonEmail);
		given(personService.existsByEmail(loginPersonEmail)).willReturn(false);
		
		OAuth2Join oAuth2Join = PersonTest.getOAuth2JoinInstance();
		
		//When
		personController.oAuth2JoinView(oAuth2Join, mav);
		
		//Then
		verifyModelAndView_oAuth2JoinViewTestCheckMav(oAuth2Join);
	}
	
	private void verifyModelAndView_oAuth2JoinViewTestCheckMav(OAuth2Join oAuth2Join) {
		then(mav).should(times(1)).addObject("formModel", oAuth2Join);
		then(mav).should(times(1)).setViewName("joinOAuth");
	}
	
	/*
	 * ModelAndView oAuth2Join(@ModelAttribute("formModel") @Valid OAuth2Join formModel)
	 */
	
	@Test
	@DisplayName("소셜 로그인한 이메일 회원 가입 처리 -> 접속한 이메일과 전송된 이메일이 불일치할 때")
	void oAuth2JoinTestNotMyResource() {
		//Given
		String loginPersonEmail = "test@test.com";
		mockingAuthorityUtilGetSessionUserEmail(loginPersonEmail);
		
		String emailInFormModel = "notmatch@test.com";
		OAuth2Join oAuth2Join = PersonTest.getOAuth2JoinInstance(emailInFormModel);
		
		//When, Then
		assertThrows(NotMyResourceException.class, () -> {
			personController.oAuth2Join(oAuth2Join);
		});
	}
	
	@Test
	@DisplayName("소셜 로그인한 이메일 회원 가입 처리 -> 정상, check PasswordEncoder")
	void oAuth2JoinTestCheckPasswordEncoder() {
		//Given
		String loginPersonEmail = "test@test.com";
		OAuth2Join oAuth2Join = mockingAuthrorityUtilAndGetOAuth2JoinInstance(loginPersonEmail);
		
		//When
		personController.oAuth2Join(oAuth2Join);
		
		//Then
		then(passwordEncoder).should(times(1)).encode(oAuth2Join.getPassword());
	}
	
	@Test
	@DisplayName("소셜 로그인한 이메일 회원 가입 처리 -> 정상, check PersonService save")
	void oAuth2JoinTestCheckPersonServiceSave() {
		//Given
		String loginPersonEmail = "test@test.com";
		OAuth2Join oAuth2Join = mockingAuthrorityUtilAndGetOAuth2JoinInstance(loginPersonEmail);
		
		//When
		personController.oAuth2Join(oAuth2Join);
		
		//Then
		then(personService).should(times(1)).save(any(Person.class));
	}
	
	@Test
	@DisplayName("소셜 로그인한 이메일 회원 가입 처리 -> 정상, check SecurityContext")
	void oAuth2JoinTestCheckSecurityContext() {
		//Given
		String loginPersonEmail = "test@test.com";
		OAuth2Join oAuth2Join = mockingAuthrorityUtilAndGetOAuth2JoinInstance(loginPersonEmail);
		
		//When
		personController.oAuth2Join(oAuth2Join);
		
		//Then
		then(securityContext).should(times(1)).setAuthentication(any(Authentication.class));
	}
	
	@Test
	@DisplayName("소셜 로그인한 이메일 회원 가입 처리 -> 정상, check RedirectedURL")
	void oAuth2JoinTestCheckRedirectedURL() {
		//Given
		String loginPersonEmail = "test@test.com";
		OAuth2Join oAuth2Join = mockingAuthrorityUtilAndGetOAuth2JoinInstance(loginPersonEmail);
		
		//When
		ModelAndView result = personController.oAuth2Join(oAuth2Join);
		
		//Then
		assertEquals("redirect:/", result.getViewName());
	}
	
	private OAuth2Join mockingAuthrorityUtilAndGetOAuth2JoinInstance(String loginPersonEmail) {
		mockingAuthorityUtilGetSessionUserEmail(loginPersonEmail);
		OAuth2Join oAuth2Join = PersonTest.getOAuth2JoinInstance(loginPersonEmail);
		return oAuth2Join;
	}
	
	private void mockingAuthorityUtilGetSessionUserEmail(String email) {
		given(securityContext.getAuthentication()).willReturn(auth);
		given(auth.getName()).willReturn(email);
	}
}