package com.gunyoung.info.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.services.domain.SpaceService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
public class SpaceControllerTest {
	
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
		
		//2번쨰 유 등록
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
		System.out.println("---------------- SpaceController Test Done ----------------");
	}
	
	/*
	 *  - 대상 메소드:
	 *  	@RequestMapping(value="/space", method= RequestMethod.GET)
	 * 		public ModelAndView myspace(ModelAndView mav)
	 */
	
	@WithAnonymousUser
	@Test
	@DisplayName("내 포트폴리오 열람 (정상-로그인 안되있을때)") 
	public void myspaceAnonymousTest() throws Exception {
		mockMvc.perform(get("/space"))
				.andExpect(status().is(302));
	}
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@DisplayName("내 포트폴리오 열람 (정상-로그인 되어있을때)")
	public void mySpaceUserTest() throws Exception {
		mockMvc.perform(get("/space"))
				.andExpect(redirectedUrl("/space/test@google.com"));
	}
	
	/*
	 *  - 대상 메소드:
	 *  	@RequestMapping(value="/space/{email}", method= RequestMethod.GET)
	 *  	public ModelAndView space(@PathVariable String email, ModelAndView mav)
	 */
	
	@WithAnonymousUser
	@Test
	@DisplayName("포트폴리오 열람 (실패-해당 email이 DB에 없을때)")
	public void spaceEmailNonExists() throws Exception {
		mockMvc.perform(get("/space/zvasf"))
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(username="second@naver.com",roles = {"USER"}) //anonymous 로 해도 동
	@Test
	@DisplayName("포트폴리오 열람 (성공-접속자가 해당 포트폴리오 주인이 아닐때)")
	public void spaceNotHostTest() throws Exception {
		mockMvc.perform(get("/space/test@google.com"))
			.andExpect(view().name("portfolio"))
			.andExpect(model().attribute("isHost", false));
	}
	
	@WithMockUser(username="test@google.com", roles = {"USER"}) 
	@Test
	@DisplayName("포트폴리오 열람 (성공-접속자가 해당 포트폴리오 주인일때)")
	public void spaceTest() throws Exception {
		mockMvc.perform(get("/space/test@google.com"))
				.andExpect(view().name("portfolio"))
				.andExpect(model().attribute("isHost", true));
	}
	
	/*
	 *  - 대상 메소드:
	 *  	@RequestMapping(value="/space/updateprofile", method = RequestMethod.GET)
	 *		public ModelAndView updateProfile(@ModelAttribute("formModel") ProfileObject profileObject, ModelAndView mav)
	 */
	
	@WithMockUser(username="none@none.com", roles= {"USER"})
	@Test
	@DisplayName("프로필 업데이트 (실패-현재 로그인 유저의 이메일이 DB에 없을때)")
	public void updateProfileEmailNonExist() throws Exception {
		mockMvc.perform(get("/space/updateprofile"))
				.andExpect(status().isNoContent());
	}
	
	@WithAnonymousUser
	@Test
	@DisplayName("프로필 업데이트 (성공-로그인 되어 있지 않을 때)")
	public void updateProfileAnonymousTest() throws Exception {
		mockMvc.perform(get("/space/updateprofile"))
				.andExpect(status().is(302));
	}
	
	@WithMockUser(username="test@google.com", roles= {"USER"}) 
	@Test
	@DisplayName("프로필 업데이트 (성공-로그인 되어 있을때)")
	public void updateProfileTest() throws Exception {
		mockMvc.perform(get("/space/updateprofile"))
				.andExpect(view().name("updateProfile"));
	}
}
