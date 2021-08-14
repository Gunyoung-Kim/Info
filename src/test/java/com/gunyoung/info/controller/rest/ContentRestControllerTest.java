package com.gunyoung.info.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import com.gunyoung.info.repos.ContentRepository;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.services.domain.SpaceService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
public class ContentRestControllerTest {
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
			for(int i=0;i<contentsNumber;i++) {
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
	
	/*
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value="/space/updatecontent/{id}", method= RequestMethod.PUT) 
	 * 		public void updateContentPost(@PathVariable long id, @ModelAttribute ContentDTO contentDto)
	 */
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 업데이트 POST (실패-입력된 id에 해당하는 content가 DB에 없을때")
	public void updateContentPostNonExist() throws Exception{
		Long nonExistContentId = Long.valueOf(1);
		
		for(Content c: contentRepository.findAll()) {
			nonExistContentId = Math.max(nonExistContentId, c.getId());
		}
		nonExistContentId++;
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("title", "changed title"); 
		map.add("contributors", "changed test contributors");
		map.add("contents", "changed test contents");
		
		mockMvc.perform(put("/space/updatecontent/"+nonExistContentId)
				.params(map))
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(username="second@naver.com", roles={"USER"})
	@Test
	@Transactional
	@DisplayName("콘텐트 업데이트 POST(실패-해당 id의 콘텐트가 현재 접속자의 것이 아닐때)")
	public void updateContentPostWrongUser() throws Exception {
		Person p = personService.findByEmail("test@google.com");
		Content content = contentRepository.findAll().get(0);
		Long contentId = content.getId();
		String title = content.getTitle();
		System.out.println(contentId+"----------");
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("hostId", String.valueOf(p.getId()));
		map.add("title", "changed title"); 
		map.add("contributors", "changed test contributors");
		map.add("contents", "changed test contents");
		
		mockMvc.perform(put("/space/updatecontent/" + contentId)
				.params(map))
				.andExpect(status().isBadRequest());
		
		content = contentService.findById(contentId);
		assertEquals(content.getTitle(),title);
	}
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("콘텐트 업데이트 POST(정상)")
	public void updateContentPostTest() throws Exception {
		Person p = personService.findByEmail("test@google.com");
		Content content = contentRepository.findAll().get(0);
		Long contentId = content.getId();
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("hostId", String.valueOf(p.getId()));
		map.add("title", "changed title"); 
		map.add("contributors", "changed test contributors");
		map.add("contents", "changed test contents");
		
		mockMvc.perform(put("/space/updatecontent/" + contentId)
				.params(map))
				.andExpect(status().isOk());
		
		content = contentService.findById(contentId);
		
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
	@Transactional
	@DisplayName("콘텐트 삭제 (실패-해당 id의 콘텐트가 현재 접속자의 것이 아닐때)")
	public void deleteContentWrongUser() throws Exception {
		Person p = personService.findByEmail("test@google.com");
		Space space = p.getSpace();
		Content content = space.getContents().get(0);
		Long contentId = content.getId();
		
		mockMvc.perform(delete("/space/deletecontent/" + contentId.intValue()))
				.andExpect(status().isBadRequest());
		
		assertEquals(contentService.existsById(contentId),true);
	}
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 삭제 (실패-입력된 id에 해당하는 content가 DB에 없을때)")
	public void deleteContentNonExist() throws Exception{
		mockMvc.perform(delete("/space/deletecontent/100"))
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(username="test@google.com", roles= {"USER"})
	@Test
	@Transactional 
	@DisplayName("콘텐트 삭제 (정상)")
	public void deleteContentTest() throws Exception {
		long num = contentService.countAll();
		
		Person p = personService.findByEmail("test@google.com");
		Space space = p.getSpace();
		Content content = space.getContents().get(0);
		Long contentId = content.getId();
		
		mockMvc.perform(delete("/space/deletecontent/" + contentId))
				.andExpect(status().isOk());
		
		assertEquals(num-1,contentService.countAll());
	}
}
