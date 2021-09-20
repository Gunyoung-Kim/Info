package com.gunyoung.info.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.repos.ContentRepository;
import com.gunyoung.info.repos.PersonRepository;
import com.gunyoung.info.repos.SpaceRepository;
import com.gunyoung.info.testutil.Integration;
import com.gunyoung.info.util.ContentTest;
import com.gunyoung.info.util.PersonTest;

/**
 * {@link ContentRestController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
@AutoConfigureMockMvc
public class ContentRestControllerTest {
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
	 *  	@RequestMapping(value="/space/makecontent/{userId}", method = RequestMethod.POST)
	 *      public void createContent(@PathVariable Long userId,@Valid @ModelAttribute("formModel") Content content)
	 */
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@DisplayName("콘텐트 추가 (실패-콘텐트 유효성 검사 통과 못함)")
	public void createContentPostNonValidate() throws Exception {
		//Given
		long givenContentNum = contentRepository.count();
		
		Long personId = person.getId();
		
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("title", ""); // 타이틀을 공백으로 보냄
		paramMap.add("contributors", "test contributors");
		paramMap.add("contents", "test contents");
		
		//When
		mockMvc.perform(post("/space/makecontent/" + personId)
				.params(paramMap))
		
		//Then
				.andExpect(status().is4xxClientError());
	
		
		assertEquals(givenContentNum, contentRepository.count());
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@DisplayName("콘텐트 추가 (실패-로그인 계정과 일치하지 않음)")
	public void createContentPostTestEmailNotMatch() throws Exception {
		//Given
		long givenContentNum = contentRepository.count();
		
		Person anotherPerson = PersonTest.getPersonInstance("second@test.com");
		personRepository.save(anotherPerson);
		Long anotherPersonId = anotherPerson.getId();
		
		// Not Empty, Not Null인 것들만 채워넣음
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("title", "testTitle");
		paramMap.add("contributors", "test contributors");
		paramMap.add("contents", "test contents");
		
		//When
		mockMvc.perform(post("/space/makecontent/" + anotherPersonId)
				.params(paramMap))
		
		//Then
				.andExpect(status().isBadRequest());
		
		assertEquals(givenContentNum, contentRepository.count());
	}
	
	@WithMockUser(username="nonexist@daum.net", roles= {"USER"})
	@Test
	@DisplayName("콘텐트 추가 (실패-일치하지만 DB에 저장되지 않은 ID)")
	public void createContentPostEmailNotExists() throws Exception {
		//Given
		long givenContentNum = contentRepository.count();
		Long nonExistPersonId = PersonTest.getNonExistPersonId(personRepository);
		
		// Not Empty, Not Null인 것들만 채워넣음
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("title", "testTitle");
		paramMap.add("contributors", "test contributors");
		paramMap.add("contents", "test contents");
		
		//When
		mockMvc.perform(post("/space/makecontent/" + nonExistPersonId)
				.params(paramMap))
		
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(givenContentNum, contentRepository.count());
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL , roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("콘텐트 추가 (실패-개인 최대 프로젝트 개수 초과)")
	public void createContentPostOverLimit() throws Exception {
		//Given
		Long personId = person.getId();
		Space space = person.getSpace();
		
		List<Content> newContentList = new ArrayList<>();
		for(int i=0;i<=Space.MAX_CONTENT_NUM;i++) {
			Content content = ContentTest.getContentInstance("title" + i);
			content.setSpace(space);
			space.getContents().add(content);
			newContentList.add(content);
		}
		contentRepository.saveAll(newContentList);
		
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("title", "testTitle");
		paramMap.add("contributors", "test contributors");
		paramMap.add("contents", "test contents");
		
		long givenContentNum = contentRepository.count();
		
		//When
		mockMvc.perform(post("/space/makecontent/" + personId)
				.params(paramMap))
		
		//Then
				.andExpect(status().isBadRequest());
		
		assertEquals(givenContentNum, contentRepository.count());
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("콘텐트 추가 (정상)")
	public void createContentPostTest() throws Exception {
		//Given
		long givenContentNum = contentRepository.count();
		Long personId = person.getId();
		
		// Not Empty, Not Null인 것들만 채워넣음
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("title", "testTitle");
		paramMap.add("contributors", "test contributors");
		paramMap.add("contents", "test contents");
		
		//When
		mockMvc.perform(post("/space/makecontent/" + personId)
				.params(paramMap))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(givenContentNum+1, contentRepository.count());
	}
	
	/*
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value="/space/updatecontent/{id}", method= RequestMethod.PUT) 
	 * 		public void updateContentPost(@PathVariable long id, @ModelAttribute ContentDTO contentDto)
	 */
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@DisplayName("콘텐트 업데이트 (실패-입력된 id에 해당하는 content가 DB에 없을때")
	public void updateContentPostNonExist() throws Exception{
		//Given
		Long nonExistContentId = ContentTest.getNonExistContentId(contentRepository);
		
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("title", "changed title"); 
		paramMap.add("contributors", "changed test contributors");
		paramMap.add("contents", "changed test contents");
		
		//When
		mockMvc.perform(put("/space/updatecontent/"+ nonExistContentId)
				.params(paramMap))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles={"USER"})
	@Test
	@Transactional
	@DisplayName("콘텐트 업데이트 (실패-해당 id의 콘텐트가 현재 접속자의 것이 아닐때)")
	public void updateContentPostWrongUser() throws Exception {
		//Given
		Person anotherPerson = PersonTest.getPersonInstance("second@test.com");
		personRepository.save(anotherPerson);
		Space space = anotherPerson.getSpace();
		
		Content content = ContentTest.getContentInstance("title");
		content.setSpace(space);
		contentRepository.save(content);
		Long contentId = content.getId();
		String title = content.getTitle();
		
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("hostId", String.valueOf(anotherPerson.getId()));
		paramMap.add("title", "changed title"); 
		paramMap.add("contributors", "changed test contributors");
		paramMap.add("contents", "changed test contents");
		
		//When
		mockMvc.perform(put("/space/updatecontent/" + contentId)
				.params(paramMap))
		
		//Then
				.andExpect(status().isBadRequest());
		
		content = contentRepository.findById(contentId).get();
		assertEquals(title, content.getTitle());
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("콘텐트 업데이트 (정상)")
	public void updateContentPostTest() throws Exception {
		//Given
		Content content = ContentTest.getContentInstance("title");
		contentRepository.save(content);
		Long contentId = content.getId();
		
		String changeTitle = "changed title";
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("hostId", String.valueOf(person.getId()));
		paramMap.add("title", changeTitle); 
		paramMap.add("contributors", "changed test contributors");
		paramMap.add("contents", "changed test contents");
		
		//When
		mockMvc.perform(put("/space/updatecontent/" + contentId)
				.params(paramMap))
		
		//Then
				.andExpect(status().isOk());
		
		content = contentRepository.findById(contentId).get();
		assertEquals(changeTitle, content.getTitle());
	}
	
	/*
	 *  - 대상 메소드:
	 *  	@RequestMapping(value="/space/deletecontent/{id}", method = RequestMethod.DELETE)
	 * 		public ModelAndView deleteContent(@PathVariable long id)
	 */
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@DisplayName("콘텐트 삭제 (실패-해당 id의 콘텐트가 현재 접속자의 것이 아닐때)")
	public void deleteContentWrongUser() throws Exception {
		//Given
		Person anotherPerson = PersonTest.getPersonInstance("second@test.com");
		personRepository.save(anotherPerson);
		Space space = anotherPerson.getSpace();
		
		Content content = ContentTest.getContentInstance("title");
		content.setSpace(space);
		contentRepository.save(content);
		Long contentId = content.getId();
		
		//When
		mockMvc.perform(delete("/space/deletecontent/" + contentId))
		
		//Then
				.andExpect(status().isBadRequest());
		
		assertEquals(true, contentRepository.existsById(contentId));
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@DisplayName("콘텐트 삭제 (실패-입력된 id에 해당하는 content가 DB에 없을때)")
	public void deleteContentNonExist() throws Exception{
		//Given
		Long nonExistContentId = ContentTest.getNonExistContentId(contentRepository);
		
		//When
		mockMvc.perform(delete("/space/deletecontent/" + nonExistContentId))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@DisplayName("콘텐트 삭제 (정상)")
	public void deleteContentTest() throws Exception {
		//Given
		Space space = person.getSpace();
		Content content = ContentTest.getContentInstance("title");
		content.setSpace(space);
		contentRepository.save(content);
		Long contentId = content.getId();
		
		long givenContentNum = contentRepository.count();
		
		//When
		mockMvc.perform(delete("/space/deletecontent/" + contentId))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(givenContentNum-1, contentRepository.count());
	}
}
