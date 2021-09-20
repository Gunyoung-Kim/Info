package com.gunyoung.info.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.enums.RoleType;
import com.gunyoung.info.repos.ContentRepository;
import com.gunyoung.info.repos.PersonRepository;
import com.gunyoung.info.repos.SpaceRepository;
import com.gunyoung.info.testutil.Integration;
import com.gunyoung.info.util.PersonTest;

/**
 * {@link PersonController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {
	
	@Autowired
	MockMvc mockMvc;

	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	SpaceRepository spaceRepository;
	
	@Autowired
	ContentRepository contentRepository;
	
	private static final String MAIN_PERSON_EMAIL = "test@test.com";
	
	private Person person;
	
	@BeforeEach
	void setup() {
		person = PersonTest.getPersonInstance(MAIN_PERSON_EMAIL);
		personRepository.save(person);
	}
	
	@AfterEach
	void tearDown() {
		contentRepository.deleteAll();
		personRepository.deleteAll();
	}
	
	/*
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value ="/", method =RequestMethod.GET)
	 * 		public ModelAndView indexViewByPage(@RequestParam(value="page",required=false,defaultValue="1") Integer page, ModelAndView mav) 
	 */
	
	@Test
	@DisplayName("메인 화면 (성공)")
	public void indexTest() throws Exception {
		//Given
		
		//When
		mockMvc.perform(get("/"))
		
		//Then
				.andExpect(view().name("index"));
	}
	
	@Test
	@DisplayName("메인 화면 페이지 (성공)")
	public void indexPageTest() throws Exception {
		//Given
		int pageNum = 1;
		
		//When
		mockMvc.perform(get("/").param("page", String.valueOf(pageNum)))
		
		//Then
				.andExpect(view().name("index"));
	}
	
	@WithMockUser(roles= {"PRE"})
	@Test
	@DisplayName("메인 화면 페이지 (성공- 소셜로그인했지만 아직 회원가입이 안되있을)")
	public void indexWithPreUser() throws Exception {
		//Given
		int pageNum = 1;
		
		//When
		mockMvc.perform(get("/").param("page", String.valueOf(pageNum)))
		
		//Then
				.andExpect(redirectedUrl("/oauth2/join"));
	}
	
	/*
	 *  - 대상 메소드:
	 *  	@RequestMapping(value = "/login", method = RequestMethod.GET)
	 * 		public String loginView() 
	 */
	
	@WithAnonymousUser
	@Test
	@DisplayName("로그인 화면 (성공)")
	public void loginViewTest() throws Exception {
		
		//When
		mockMvc.perform(get("/login"))
		
		//Then
				.andExpect(view().name("login"));
	}
	
	/*
	 *  - 대상 메소드:
	 *  	@RequestMapping(value="/join" , method = RequestMethod.GET)
	 * 		public ModelAndView joinView(@ModelAttribute("formModel") Person person, ModelAndView mav)
	 */
	
	@Test
	@DisplayName("회원가입 화면 (성공)")
	public void joinViewTest() throws Exception {
		
		//When
		mockMvc.perform(get("/join"))
		
		//Then
				.andExpect(view().name("join"));
	}
	
	/*
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value="/join", method = RequestMethod.POST)
	 * 		public ModelAndView join(@Valid @ModelAttribute("formModel") Person person,BindingResult result, ModelAndView mav)
	 */
	
	@Test
	@Transactional
	@DisplayName("회원가입 (실패-이미 존재하는 이메일로 회원 가입시도: 잘못된 경로 대비)")
	public void joinPostEmailDuplicated() throws Exception {
		//Given
		long givenPersonNum = personRepository.count();
		
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		String existPersonEmail = person.getEmail();
		paramMap.add("email", existPersonEmail); 
		paramMap.add("password", "abcd1234");
		paramMap.add("firstName", "first");
		paramMap.add("lastName", "last");
		
		//When
		mockMvc.perform(post("/join")
				.params(paramMap))
		
		//Then
				.andExpect(status().isConflict());
		
		assertEquals(givenPersonNum, personRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("회원가입 (실패-유효성 검증 실패, 이메일이 형식에 맞지 않음)")
	public void joinPostNonValidateEmail() throws Exception {
		//Given
		long givenPersonNum = personRepository.count();
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("email", "second"); //이메일 형식 맞지 않음
		paramMap.add("password", "abcd1234");
		paramMap.add("firstName", "first");
		paramMap.add("lastName", "last");
		
		//When
		mockMvc.perform(post("/join")
				.params(paramMap))
		
		//Then
				.andExpect(status().is4xxClientError());
		
		assertEquals(givenPersonNum,personRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("회원가입 (실패-유효성 검증 실패, 비밀번호가 형식에 맞지않음- 공백)")
	public void joinNonValidatePassword() throws Exception {
		//Given
		long givenPersonNum = personRepository.count();
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap = new LinkedMultiValueMap<>();
		paramMap.add("email", "second@naver.com"); 
		paramMap.add("password", ""); // 비밀번호 공백
		paramMap.add("firstName", "first");
		paramMap.add("lastName", "last");
		
		//When
		mockMvc.perform(post("/join")
				.params(paramMap))
		
		//Then
				.andExpect(status().is4xxClientError());
		
		assertEquals(givenPersonNum,personRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("회원가입 (실패-유효성 검증 실패, 이름 공백)")
	public void joinNonValidateFirstName() throws Exception {
		//Given
		long givenPersonNum = personRepository.count();
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap = new LinkedMultiValueMap<>();
		paramMap.add("email", "second@naver.com"); 
		paramMap.add("password", "abcd1234"); 
		paramMap.add("firstName", "");   // 이름 공백
		paramMap.add("lastName", "last");
		
		//When
		mockMvc.perform(post("/join")
				.params(paramMap))
		
		//Then
				.andExpect(status().is4xxClientError());
		
		assertEquals(givenPersonNum,personRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("회원가입 (성공, person 개수 확인)")
	public void joinTestCheckPersonNum() throws Exception {
		//Given
		long personNum = personRepository.count();
		MultiValueMap<String, String> paramMap = getJoinSuccessParamMap();
		
		//When
		mockMvc.perform(post("/join")
				.params(paramMap))
		
		//Then
				.andExpect(redirectedUrl("/"));
		
		assertEquals(personNum+1,personRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("회원가입 POST (성공, Person DB에 존재 여부 확인)")
	public void joinTestCheckIsInDB() throws Exception {
		//Given
		MultiValueMap<String, String> paramMap = getJoinSuccessParamMap();
		
		//When
		mockMvc.perform(post("/join")
				.params(paramMap))
		
		//Then
				.andExpect(redirectedUrl("/"));
		
		assertEquals(personRepository.existsByEmail("third@naver.com"),true);
	}
	
	@Test
	@Transactional
	@DisplayName("회원가입 POST (성공, 추가된 Person Role이 USER인지 확인)")
	public void joinTestCheckIsROLE_USER() throws Exception {
		//Given
		MultiValueMap<String, String> paramMap = getJoinSuccessParamMap();
		
		//When
		mockMvc.perform(post("/join")
				.params(paramMap))
		
		//Then
				.andExpect(redirectedUrl("/"));
		
		assertEquals(personRepository.findByEmail("third@naver.com").get().getRole(),RoleType.USER);
	}
	
	private MultiValueMap<String, String> getJoinSuccessParamMap() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("email", "third@naver.com"); 
		map.add("password", "abcd1234");
		map.add("firstName", "first");
		map.add("lastName", "last");
		
		return map;
	}
	
	/* 
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value= "/oauth2/join" , method = RequestMethod.GET) 
	 *		public ModelAndView oAuth2JoinView(@ModelAttribute("formModel") @Valid OAuth2Join formModel, ModelAndView mav)
	 */
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"PRE"})
	@Test
	@Transactional
	@DisplayName("소셜 로그인 회원가입 (실패-이미 회원가입 되어있는 회원의 접근)")
	public void oAuth2JoinViewAlreadyJoin() throws Exception {
		//When
		mockMvc.perform(get("/oauth2/join"))
		
		//Then
				.andExpect(status().isConflict());
		
	}
	
	/*
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value="/oauth2/join", method = RequestMethod.POST) 
	 * 		public ModelAndView oAuth2Join(@ModelAttribute("formModel") @Valid OAuth2Join formModel)
	 */
	@WithMockUser(username="none@google.com", roles= {"PRE"})
	@Test
	@Transactional
	@DisplayName("소셜로그인 계정 회원가입 POST (실패- 이메일이 입력된 사항과 불일치)")
	public void oAuth2JoinEmailNotMatch() throws Exception{
		//Given
		long givenPersonNum = personRepository.count();
		
		MultiValueMap<String,String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("email","oAuth2JoinPostEmailNotMatch@mail.com");
		paramMap.add("firstName", "new");
		paramMap.add("lastName", "one");
		paramMap.add("password", "abcd1234");
		
		//When
		mockMvc.perform(post("/oauth2/join")
				.params(paramMap))
		
		//Then
				.andExpect(status().isBadRequest());
		
		assertEquals(givenPersonNum, personRepository.count());
	}
	
	@WithMockUser(username="new@google.com" , roles= {"PRE"})
	@Test
	@Transactional
	@DisplayName("소셜로그인 계정 회원가입 POST (성공, Person 개수 확인)")
	public void oAuth2JoinTestCheckPersonNum() throws Exception {
		//Given
		long givnePersonNum = personRepository.count();
		
		MultiValueMap<String,String> paramMap = getOAuth2JoinSuccessParamMap();
		
		//When
		mockMvc.perform(post("/oauth2/join")
				.params(paramMap))
		
		//Then
				.andExpect(redirectedUrl("/"))
				.andExpect(authenticated().withRoles("USER").withUsername("new@google.com"));
		
		assertEquals(givnePersonNum+1,personRepository.count());
	}
	
	@WithMockUser(username="new@google.com" , roles= {"PRE"})
	@Test
	@Transactional
	@DisplayName("소셜로그인 계정 회원가입 POST (성공, Person DB에 있는지 확인)")
	public void oAuth2JoinTestCheckIsInDB() throws Exception {
		//Given
		MultiValueMap<String,String> paramMap = getOAuth2JoinSuccessParamMap();
		
		//When
		mockMvc.perform(post("/oauth2/join")
				.params(paramMap))
		
		//Then
				.andExpect(redirectedUrl("/"))
				.andExpect(authenticated().withRoles("USER").withUsername("new@google.com"));
		
		assertEquals(personRepository.existsByEmail("new@google.com"),true);
	}
	
	private MultiValueMap<String,String> getOAuth2JoinSuccessParamMap() {
		MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
		map.add("email","new@google.com");
		map.add("firstName", "new");
		map.add("lastName", "one");
		map.add("password", "abcd1234");
		
		return map;
	}
}
