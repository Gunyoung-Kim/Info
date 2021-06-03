package com.gunyoung.info.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.services.ContentService;
import com.gunyoung.info.services.PersonService;
import com.gunyoung.info.services.SpaceService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
public class ContentControllerTest {
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
		
		//2번쨰 회원 등록
		if(!personService.existsByEmail("second@naver.com")) {
			Person person2 = new Person();
			person2.setEmail("second@naver.com");
			person2.setPassword("abcd1234");
			person2.setFirstName("로그");
			person2.setLastName("블");
			
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
		System.out.println("---------------- ContentController Test Done ----------------");
	}
	
	/*
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value="/space/makecontent/{email}", method = RequestMethod.GET)
	 *  	public ModelAndView createContent(@PathVariable String email,@ModelAttribute("formModel") Content content, ModelAndView mav)
	 */
	
	@WithMockUser(username="second@naver.com", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 추가 (실패-로그인 계정과 일치하지 않음)")
	public void createContentTestEmailNotMatch() throws Exception {
		mockMvc.perform(get("/space/makecontent/test@google.com"))
			   .andExpect(redirectedUrl("/errorpage"));
	}
	
	@WithMockUser(username="nonexist@daum.net", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 추가 (실패-일치하지만 DB에 저장되지 않은 이메일)")
	public void createContentEmailNotExists() throws Exception {
		mockMvc.perform(get("/space/makecontent/nonexist@daum.net"))
				.andExpect(redirectedUrl("/errorpage"));
	}
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 추가 (정상)")
	public void craeteContentTest() throws Exception{
		mockMvc.perform(get("/space/makecontent/test@google.com"))
				.andExpect(status().isOk())
				.andExpect(view().name("createContent"));
	}
	
	/*
	 *  - 대상 메소드:
	 *  	@RequestMapping(value="/space/makecontent/{email}", method = RequestMethod.POST)
	 *      public ModelAndView createContentPost(@PathVariable String email,@Valid @ModelAttribute("formModel") Content content ,ModelAndView mav)
	 */
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 추가 POST (실패-콘텐트 유효성 검사 통과 못함)")
	public void createContentPostNonValidate() throws Exception {
		long contentNum = contentService.countAll();
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("title", ""); // 타이틀을 공백으로 보냄
		map.add("contributors", "test contributors");
		map.add("contents", "test contents");
		
		
		mockMvc.perform(post("/space/makecontent/test@google.com")
				.params(map))
				.andExpect(status().is4xxClientError());
	
		
		assertEquals(contentService.countAll(),contentNum);
	}
	
	@WithMockUser(username="second@naver.com", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 추가 POST (실패-로그인 계정과 일치하지 않음)")
	public void createContentPostTestEmailNotMatch() throws Exception {
		long contentNum = contentService.countAll();
		
		// Not Empty, Not Null인 것들만 채워넣음
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("title", "testTitle");
		map.add("contributors", "test contributors");
		map.add("contents", "test contents");
		
		mockMvc.perform(post("/space/makecontent/test@google.com")
				.params(map))
				.andExpect(redirectedUrl("/errorpage"));
		
		assertEquals(contentService.countAll(),contentNum);
	}
	
	@WithMockUser(username="nonexist@daum.net", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 추가 POST (실패-일치하지만 DB에 저장되지 않은 이메일)")
	public void createContentPostEmailNotExists() throws Exception {
		long contentNum = contentService.countAll();
		
		// Not Empty, Not Null인 것들만 채워넣음
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("title", "testTitle");
		map.add("contributors", "test contributors");
		map.add("contents", "test contents");
		
		mockMvc.perform(post("/space/makecontent/test@google.com")
				.params(map))
				.andExpect(redirectedUrl("/errorpage"));
		
		assertEquals(contentService.countAll(),contentNum);
	}
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("콘텐트 추가 POST (정상)")
	public void createContentPostTest() throws Exception {
		long contentNum = contentService.countAll();
		Person person = personService.findByEmail("test@google.com");
		Space space = person.getSpace();
		long spaceContentsSize = space.getContents().size();
		// Not Empty, Not Null인 것들만 채워넣음
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("title", "testTitle");
		map.add("contributors", "test contributors");
		map.add("contents", "test contents");
		
		mockMvc.perform(post("/space/makecontent/test@google.com")
				.params(map))
				.andExpect(redirectedUrl("/space"));
		
		// content 테이블의 로우 개수 추가 됐는지 확인
		assertEquals(contentService.countAll(),contentNum+1);
		
		//해당 유저의 포트폴리오에 하나 더 추가 되었는지 확인
		assertEquals(space.getContents().size(),spaceContentsSize+1);
		
	}
	
	/*
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value="/space/updatecontent/{id}", method= RequestMethod.GET)
	 *		public ModelAndView updateContent(@PathVariable long id, @ModelAttribute("formModel") ContentDTO contentDto, ModelAndView mav)
	 */
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 업데이트 (실패-입력된 id에 해당하는 content가 DB에 없을때)")
	public void updateContentNonExist() throws Exception{
		mockMvc.perform(get("/space/updatecontent/100"))
				.andExpect(redirectedUrl("/errorpage"));
	}
	
	@WithMockUser(username="second@naver.com", roles={"USER"})
	@Test
	@DisplayName("콘텐트 업데이트 (실패-해당 id의 콘텐트가 현재 접속자의 것이 아닐때)")
	public void updateContentWrongUser() throws Exception {
		mockMvc.perform(get("/space/updatecontent/1"))
				.andExpect(redirectedUrl("/errorpage"));
	}
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 업데이트 (정상)")
	public void updateContentTest() throws Exception {
		mockMvc.perform(get("/space/updatecontent/1"))
				.andExpect(view().name("updateContent"));
	}
	
	/*
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value="/space/updatecontent/{id}", method= RequestMethod.POST) 
	 *		public ModelAndView updateContentPost(@PathVariable long id, @ModelAttribute("formModel") ContentDTO contentDto, ModelAndView mav)
	 */
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 업데이트 POST (실패-입력된 id에 해당하는 content가 DB에 없을때")
	public void updateContentPostNonExist() throws Exception{
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("title", "changed title"); 
		map.add("contributors", "changed test contributors");
		map.add("contents", "changed test contents");
		
		mockMvc.perform(post("/space/updatecontent/100")
				.params(map))
				.andExpect(redirectedUrl("/errorpage"));
	}
	
	@WithMockUser(username="second@naver.com", roles={"USER"})
	@Test
	@Transactional
	@DisplayName("콘텐트 업데이트 POST(실패-해당 id의 콘텐트가 현재 접속자의 것이 아닐때)")
	public void updateContentPostWrongUser() throws Exception {
		Content content = contentService.findById(Long.valueOf(1));
		String title = content.getTitle();
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("hostEmail", "test@google.com");
		map.add("title", "changed title"); 
		map.add("contributors", "changed test contributors");
		map.add("contents", "changed test contents");
		
		mockMvc.perform(post("/space/updatecontent/1")
				.params(map))
				.andExpect(redirectedUrl("/errorpage"));
		
		content = contentService.findById(Long.valueOf(1));
		assertEquals(content.getTitle(),title);
	}
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("콘텐트 업데이트 POST(정상)")
	public void updateContentPostTest() throws Exception {
		Content content = contentService.findById(Long.valueOf(1));
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("hostEmail", "test@google.com");
		map.add("title", "changed title"); 
		map.add("contributors", "changed test contributors");
		map.add("contents", "changed test contents");
		
		mockMvc.perform(post("/space/updatecontent/1")
				.params(map))
				.andExpect(redirectedUrl("/space"));
		
		content = contentService.findById(Long.valueOf(1));
		
		assertEquals(content.getTitle(),"changed title");
		assertEquals(content.getContributors(),"changed test contributors");
		assertEquals(content.getContents(),"changed test contents");
	}
	
	/*
	 *  - 대상 메소드:
	 *  	@RequestMapping(value="/space/deletecontent/{id}", method = RequestMethod.DELETE)
	 * 		public ModelAndView deleteContent(@PathVariable long id)
	 */
	
	@WithMockUser(username="second@naver.com", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 삭제 (실패-해당 id의 콘텐트가 현재 접속자의 것이 아닐때)")
	public void deleteContentWrongUser() throws Exception {
		mockMvc.perform(delete("/space/deletecontent/1"))
				.andExpect(redirectedUrl("/errorpage"));
		
		assertEquals(contentService.existsById(Long.valueOf(1)),true);
	}
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 삭제 (실패-입력된 id에 해당하는 content가 DB에 없을때)")
	public void deleteContentNonExist() throws Exception{
		mockMvc.perform(delete("/space/deletecontent/100"))
				.andExpect(redirectedUrl("/errorpage"));
	}
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@Transactional 
	@DisplayName("콘텐트 삭제 (정상)")
	public void deleteContentTest() throws Exception {
		long num = contentService.countAll();
		Content content = contentService.findById(Long.valueOf(1));
		Space space = content.getSpace();
		int contentsListSize = space.getContents().size();
		mockMvc.perform(delete("/space/deletecontent/1"))
				.andExpect(redirectedUrl("/space"));
		
		assertEquals(contentsListSize-1, space.getContents().size());
		assertEquals(num-1,contentService.countAll());
	}
}
