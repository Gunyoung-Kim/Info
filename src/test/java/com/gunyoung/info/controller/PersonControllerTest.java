package com.gunyoung.info.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.enums.RoleType;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.services.domain.SpaceService;

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
			
			// space 내용 설정
			Space space = person.getSpace();
			space.setDescription("test용 자기소개입니다.");
			space.setGithub("https://github.com/Gunyoung-Kim");
			
			// content 들 설정
			int contentsNumber = 1;
			for(int i=0;i<=contentsNumber;i++) {
				Content content = new Content();
				content.setTitle(i+" 번째 타이틀");
				content.setDescription(i+" 번째 프로젝트 설명");
				content.setContributors(i+" 번째 기여자들");
				content.setContents(i+ " 번째 프로젝트 내용");
				content.setSpace(space);
				contentService.save(content);
				
				space.getContents().add(content);
			}
			
			spaceService.save(space);
		}
		//2번쨰 유저 등록
		if(!personService.existsByEmail("second@naver.com")) {
			Person person2 = new Person();				
			person2.setEmail("second@naver.com");	
			person2.setPassword("abcd1234");
			person2.setFirstName("로그");
			person2.setLastName("블");
							
			// space 내용 설정
			Space space2 = person2.getSpace();
			space2.setDescription("test2222용 자기소개입니다.");
			space2.setGithub("https://github.com/Gunyoung-Kim");
							
			personService.save(person2);
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
	 * 		public ModelAndView indexByPage(@RequestParam(value="page",required=false,defaultValue="1") Integer page, ModelAndView mav) 
	 */
	
	@Test
	@DisplayName("메인 화면 (성공)")
	public void indexTest() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(view().name("index"));
		
		mockMvc.perform(get("/?page=1"))
				.andExpect(view().name("index"));
	}
	
	@Test
	@DisplayName("메인 화면 페이지 (성공)")
	public void indexPageTest() throws Exception {
		mockMvc.perform(get("/").param("page", "1"))
				.andExpect(view().name("index"));
	}
	
	@WithMockUser(roles= {"PRE"})
	@Test
	@DisplayName("메인 화면 페이지 (성공- 소셜로그인했지만 아직 회원가입이 안되있을)")
	public void indexWithPreUser() throws Exception {
		mockMvc.perform(get("/").param("page", "1"))
				.andExpect(redirectedUrl("/oauth2/join"));
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
		map.add("email", "third@naver.com"); 
		map.add("password", "abcd1234");
		map.add("firstName", "first");
		map.add("lastName", "last");
		
		mockMvc.perform(post("/join")
				.params(map))
				.andExpect(redirectedUrl("/"));
		
		assertEquals(personNum+1,personService.countAll());
		assertEquals(personService.existsByEmail("third@naver.com"),true);
		assertEquals(personService.findByEmail("third@naver.com").getRole(),RoleType.USER);
	}
	
	/* 
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value= "/oauth2/join" , method = RequestMethod.GET) 
	 *		public ModelAndView oAuth2Join(@ModelAttribute("formModel") @Valid OAuth2Join formModel, ModelAndView mav)
	 */
	@WithMockUser(username="test@google.com", roles= {"PRE"})
	@Test
	@DisplayName("소셜 로그인 회원가입 (실패-이미 회원가입 되어있는 회원의 접근)")
	public void oAuth2JoinAlreadyJoin() throws Exception {
		mockMvc.perform(get("/oauth2/join"))
				.andExpect(redirectedUrl("/errorpage"));
		
	}
	
	/*
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value="/oauth2/join", method = RequestMethod.POST) 
	 * 		public ModelAndView oAuth2JoinPost(@ModelAttribute("formModel") @Valid OAuth2Join formModel)
	 */
	@WithMockUser(username="none@google.com", roles= {"PRE"})
	@Test
	@DisplayName("소셜로그인 계정 회원가입 POST (실패- 이메일이 입력된 사항과 불일치)")
	public void oAuth2JoinPostEmailNotMatch() throws Exception{
		
		long personNum = personService.countAll();
		
		MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
		map.add("email","oAuth2JoinPostEmailNotMatch@mail.com");
		map.add("firstName", "new");
		map.add("lastName", "one");
		map.add("password", "abcd1234");
		
		mockMvc.perform(post("/oauth2/join")
				.params(map))
				.andExpect(redirectedUrl("/errorpage"));
		
		assertEquals(personNum, personService.countAll());
	}
	
	@WithMockUser(username="new@google.com" , roles= {"PRE"})
	@Test
	@DisplayName("소셜로그인 계정 회원가입 POST (성공)")
	public void oAuth2JoinPostTest() throws Exception {
		long personNum = personService.countAll();
		
		MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
		map.add("email","new@google.com");
		map.add("firstName", "new");
		map.add("lastName", "one");
		map.add("password", "abcd1234");
		
		mockMvc.perform(post("/oauth2/join")
				.params(map))
				.andExpect(redirectedUrl("/"))
				.andExpect(authenticated().withRoles("USER").withUsername("new@google.com"));
		
		assertEquals(personNum+1,personService.countAll());
		assertEquals(personService.existsByEmail("new@google.com"),true);
	}
	
	
	/*
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value="/withdraw", method=RequestMethod.DELETE)
	 * 		public ModelAndView personWithdraw(@RequestParam("email") String email,ModelAndView mav)
	 */
	
	@WithMockUser(username="none@google.com", roles= {"USER"})
	@Test
	@DisplayName("회원탈퇴 DELETE (실패-해당 계정이 DB에 존재하지 않을때)")
	public void personWithdrawNonExist() throws Exception {
		mockMvc.perform(delete("/withdraw")
				.param("email", "none@google.com"))
				.andExpect(redirectedUrl("/errorpage"));
	}
	
	@WithMockUser(username="second@naver.com", roles= {"USER"})
	@Test
	@DisplayName("회원탈퇴 DELETE (실패-로그인 계정이 탈퇴 계정과 일치하지 않을때")
	public void personWithdrawNotMatch() throws Exception {
		mockMvc.perform(delete("/withdraw")
				.param("email", "test@google.com"))
				.andExpect(redirectedUrl("/errorpage"));
	}
	
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("회원탈퇴 DELETE (성공)")
	public void personWithdrawTest() throws Exception {
		Person person = personService.findByEmail("test@google.com");
		Space space = person.getSpace();
		Long spaceId = space.getId();
		Content content = space.getContents().get(0);
		Long contentId = content.getId();
		mockMvc.perform(delete("/withdraw")
				.param("email", "test@google.com"))
				.andExpect(redirectedUrl("/logout"));
		
		assertEquals(personService.existsByEmail("test@google.com"),false);
		assertEquals(spaceService.existsById(spaceId),false);
		assertEquals(contentService.existsById(contentId),false);
	}
}
