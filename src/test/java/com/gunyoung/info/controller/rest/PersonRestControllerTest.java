package com.gunyoung.info.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.services.domain.SpaceService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
public class PersonRestControllerTest {
	
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
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(username="second@naver.com", roles= {"USER"})
	@Test
	@DisplayName("회원탈퇴 DELETE (실패-로그인 계정이 탈퇴 계정과 일치하지 않을때")
	public void personWithdrawNotMatch() throws Exception {
		mockMvc.perform(delete("/withdraw")
				.param("email", "test@google.com"))
				.andExpect(status().isBadRequest());
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
				.andExpect(status().isOk());
		
		assertEquals(personService.existsByEmail("test@google.com"),false);
		assertEquals(spaceService.existsById(spaceId),false);
		assertEquals(contentService.existsById(contentId),false);
	}
}
