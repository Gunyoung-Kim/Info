package com.gunyoung.info.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.repos.ContentRepository;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.services.domain.SpaceService;

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
	
	@Autowired
	ContentRepository contentRepository;
	
	public static final int MAX_CONTENT_NUM = 50;
	
	private static final int INIT_CONTENT_NUM = 3;
	
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
			int contentsNumber = INIT_CONTENT_NUM;
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
	 *  	@RequestMapping(value="/space/makecontent/{userId}", method = RequestMethod.GET)
	 *  	public ModelAndView createContentView(@PathVariable Long userId,@ModelAttribute("formModel") Content content, ModelAndView mav)
	 */
	
	@WithMockUser(username="second@naver.com", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 추가 (실패-로그인 계정과 일치하지 않음)")
	public void createContentTestEmailNotMatch() throws Exception {
		//Given
		Person anotherPerson = personService.findByEmail("test@google.com");
		Long anotherID = anotherPerson.getId();
		
		//When
		mockMvc.perform(get("/space/makecontent/" + anotherID))
		
		//Then
			   .andExpect(status().isBadRequest());
	}
	
	@WithMockUser(username="nonexist@daum.net", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 추가 (실패-일치하지만 DB에 저장되지 않은 ID)")
	public void createContentEmailNotExists() throws Exception {
		//Given
		Long nonExistId = getNonExistPersonId();
		
		//When
		mockMvc.perform(get("/space/makecontent/" + nonExistId))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("콘텐트 추가 (실패-개인 최대 프로젝트 개수 초과)")
	public void createContentEmailOverLimit() throws Exception {
		Person person = personService.findByEmail("test@google.com");
		Long personId = person.getId();
		Space space = person.getSpace();
		for(int i=INIT_CONTENT_NUM;i<=MAX_CONTENT_NUM;i++) {
			Content content = new Content();
			content.setTitle(i+" 번째 타이틀");
			content.setDescription(i+" 번째 프로젝트 설명");
			content.setContributors(i+" 번째 기여자들");
			content.setContents(i+ " 번째 프로젝트 내용");
			spaceService.addContent(space, content);
		}
		
		mockMvc.perform(get("/space/makecontent/"+personId))
				.andExpect(status().isBadRequest());
	}
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 추가 (정상)")
	public void craeteContentTest() throws Exception{
		//Given
		Person person = personService.findByEmail("test@google.com");
		Long personId = person.getId();
		
		//When
		mockMvc.perform(get("/space/makecontent/" + personId))
		
		//Then
				.andExpect(status().isOk())
				.andExpect(view().name("createContent"));
	}
	
	/*
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value="/space/updatecontent/{id}", method= RequestMethod.GET)
	 *		public ModelAndView updateContentView(@PathVariable long id, @ModelAttribute("formModel") ContentDTO contentDto, ModelAndView mav)
	 */
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 업데이트 (실패-입력된 id에 해당하는 content가 DB에 없을때)")
	public void updateContentNonExist() throws Exception{
		mockMvc.perform(get("/space/updatecontent/100"))
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(username="second@naver.com", roles={"USER"})
	@Test
	@DisplayName("콘텐트 업데이트 (실패-해당 id의 콘텐트가 현재 접속자의 것이 아닐때)")
	public void updateContentWrongUser() throws Exception {
		//Given
		Content content = contentRepository.findAll().get(0);
		Long contentId = content.getId();
		
		//When
		mockMvc.perform(get("/space/updatecontent/"+contentId))
		
		//Then
				.andExpect(status().isBadRequest());
	}
	
	@Disabled
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("콘텐트 업데이트 (정상)")
	public void updateContentTest() throws Exception {
		//Given
		Person p = personService.findByEmail("test@google.com");
		Space space = p.getSpace();
		Content content = space.getContents().get(0);
		Long contentId = content.getId();
		
		//When
		mockMvc.perform(get("/space/updatecontent/" + contentId))
		
		//Then
				.andExpect(view().name("updateContent"));
	}
	
	private Long getNonExistPersonId() {
		Long nonExistPersonId = Long.valueOf(1);
		
		for(Person p : personService.findAll()) {
			nonExistPersonId = Math.max(nonExistPersonId, p.getId());
		}
		
		nonExistPersonId++;
		
		return nonExistPersonId;
	}
}
