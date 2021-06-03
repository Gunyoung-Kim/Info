package com.gunyoung.info.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.services.ContentService;
import com.gunyoung.info.services.PersonService;
import com.gunyoung.info.services.SpaceService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
public class PersonControllerTest {
	@Autowired
	MockMvc mockMvc;

	@Autowired
	PersonService personService;
	
	@Autowired
	SpaceService spaceService;
	
	@Autowired
	ContentService contentService;
	
	@BeforeEach
	void setup() {
		// 유저 등록
		if(!personService.existsByEmail("test@google.com")) {
			Person person = new Person();
			person.setEmail("test@google.com");
			person.setPassword("abcd1234");
			person.setFirstName("스트");
			person.setLastName("테");
										
			personService.save(person);
		}
	}
	
	@AfterEach
	void tearDown() {
	}
	
	@AfterAll
	static void done() {
		System.out.println("---------------- PersonController Test Done ----------------");
	}
	
	/*
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value ="/", method =RequestMethod.GET)
	 * 		public String index() 
	 */
	
	@Test
	@DisplayName("메인 화면 (성공)")
	public void indexTest() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(view().name("index"));
	}
	
	/*
	 *  - 대상 메소드:
	 *  	@RequestMapping(value = "/login", method = RequestMethod.GET)
	 * 		public String login() 
	 */
	
	@WithAnonymousUser
	@Test
	@DisplayName("로그인 화면 (성공)")
	public void loginTest() throws Exception {
		mockMvc.perform(get("/login"))
				.andExpect(view().name("login"));
	}
	
	/*
	 *  - 대상 메소드:
	 *  	@RequestMapping(value="/join" , method = RequestMethod.GET)
	 * 		public ModelAndView join(@ModelAttribute("formModel") Person person, ModelAndView mav)
	 */
	
	@Test
	@DisplayName("회원가입 화면 (성공)")
	public void joinTest() throws Exception {
		mockMvc.perform(get("/join"))
				.andExpect(view().name("join"));
	}
	
	/*
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value="/join", method = RequestMethod.POST)
	 * 		public ModelAndView joinPost(@Valid @ModelAttribute("formModel") Person person,BindingResult result, ModelAndView mav)
	 */
	
	@Test
	@DisplayName("회원가입 POST (실패-이미 존재하는 이메일로 회원 가입시도: 잘못된 경로 대비)")
	public void joinPostEmailDuplicated() throws Exception {
		long personNum = personService.countAll();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("email", "test@google.com"); //이메일 형식 맞지 않음
		map.add("password", "abcd1234");
		map.add("firstName", "first");
		map.add("lastName", "last");
		
		mockMvc.perform(post("/join")
				.params(map))
				.andExpect(redirectedUrl("/errorpage"));
			
		assertEquals(personNum,personService.countAll());
	}
	
	@Test
	@DisplayName("회원가입 POST (실패-유효성 검증 실패)")
	public void joinPostNonValidate() throws Exception {
		long personNum = personService.countAll();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("email", "second"); //이메일 형식 맞지 않음
		map.add("password", "abcd1234");
		map.add("firstName", "first");
		map.add("lastName", "last");
		
		mockMvc.perform(post("/join")
				.params(map))
				.andExpect(status().is4xxClientError());
		
		
		assertEquals(personNum,personService.countAll());
		
		map = new LinkedMultiValueMap<>();
		map.add("email", "second@naver.com"); 
		map.add("password", ""); // 비밀번호 공백
		map.add("firstName", "first");
		map.add("lastName", "last");
		
		mockMvc.perform(post("/join")
				.params(map))
				.andExpect(status().is4xxClientError());
		
		
		assertEquals(personNum,personService.countAll());
		
		map = new LinkedMultiValueMap<>();
		map.add("email", "second@naver.com"); 
		map.add("password", "abcd1234"); 
		map.add("firstName", "");   // 이름 공백
		map.add("lastName", "last");
		
		mockMvc.perform(post("/join")
				.params(map))
				.andExpect(status().is4xxClientError());
		
		
		assertEquals(personNum,personService.countAll());
	}
	
	@Test
	@Transactional
	@DisplayName("회원가입 POST (성공)")
	public void joinPostTest() throws Exception {
		long personNum = personService.countAll();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("email", "second@naver.com"); 
		map.add("password", "abcd1234");
		map.add("firstName", "first");
		map.add("lastName", "last");
		
		mockMvc.perform(post("/join")
				.params(map))
				.andExpect(redirectedUrl("/"));
		
		assertEquals(personNum+1,personService.countAll());
		assertEquals(personService.existsByEmail("second@naver.com"),true);
	
	}
}
