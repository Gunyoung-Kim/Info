package com.gunyoung.info.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.services.domain.SpaceService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
public class SpaceRestControllerTest {
	
	
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
	
	/*
	 *  - 대상 메소드:
	 *  	@RequestMapping(value="/space/updateprofile", method = RequestMethod.PUT)
	 * 		public void updateProfilePost(@ModelAttribute("formModel") @Valid ProfileDTO profileDTO)
	 */
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Transactional
	@Test
	@DisplayName("프로필 업데이트 (실패-유효성 검사 불통과)")
	public void updateProfilePostNonValidate() throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("email", "test@google.com");
		map.add("firstName", ""); // 이름을 일부러 비움
		map.add("lastName", "구");
		map.add("description", "changed description");
		map.add("github","changed github");
		map.add("instagram", "changed instagram");
		map.add("tweeter", "changed tweeter");
		map.add("facebook", "changed facebook");
		
		mockMvc.perform(put("/space/updateprofile")
				.params(map))
				.andExpect(status().is4xxClientError());
		
		Person person = personService.findByEmail("test@google.com");
		Space space = person.getSpace();
		assertEquals(person.getFirstName(),"스트");
		assertEquals(person.getLastName(),"테");
		assertEquals(space.getDescription(),"test용 자기소개입니다.");
		assertEquals(space.getGithub(),"https://github.com/Gunyoung-Kim");
	}
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("프로필 업데이트 (성공)")
	public void updateProfilePostTest() throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("email", "test@google.com");
		map.add("firstName", "변화");
		map.add("lastName", "구");
		map.add("description", "changed description");
		map.add("github","changed github");
		map.add("instagram", "changed instagram");
		map.add("tweeter", "changed tweeter");
		map.add("facebook", "changed facebook");
		
		mockMvc.perform(put("/space/updateprofile")
				.params(map))
				.andExpect(status().isOk());
		
		Person person = personService.findByEmail("test@google.com");
		Space space = person.getSpace();
		assertEquals(person.getFirstName(),"변화");
		assertEquals(person.getLastName(),"구");
		assertEquals(space.getDescription(),"changed description");
		assertEquals(space.getGithub(),"changed github");
		assertEquals(space.getInstagram(),"changed instagram");
		assertEquals(space.getTweeter(),"changed tweeter");
		assertEquals(space.getFacebook(),"changed facebook");
	}
}
