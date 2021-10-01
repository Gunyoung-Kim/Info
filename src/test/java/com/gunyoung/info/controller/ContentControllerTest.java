package com.gunyoung.info.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
 * {@link ContentController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
@AutoConfigureMockMvc
public class ContentControllerTest {
	
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
	 *  	@RequestMapping(value="/space/makecontent/{userId}", method = RequestMethod.GET)
	 *  	public ModelAndView createContentView(@PathVariable Long userId,@ModelAttribute("formModel") Content content, ModelAndView mav)
	 */

	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("콘텐트 추가 (실패-로그인 계정과 일치하지 않음)")
	public void createContentTestEmailNotMatch() throws Exception {
		//Given
		String anoterPersonEmail = "second@test.com";
		Person anotherPerson = PersonTest.getPersonInstance(anoterPersonEmail);
		personRepository.save(anotherPerson);
		
		Long anotherPersonID = anotherPerson.getId();
		
		//When
		mockMvc.perform(get("/space/makecontent/" + anotherPersonID))
		
		//Then
			   .andExpect(status().isBadRequest());
	}
	
	@WithMockUser(username="nonexist@daum.net", roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("콘텐트 추가 (실패-일치하지만 DB에 저장되지 않은 Person ID)")
	public void createContentEmailNotExists() throws Exception {
		//Given
		Long nonExistPersonId = PersonTest.getNonExistPersonId(personRepository);
		
		//When
		mockMvc.perform(get("/space/makecontent/" + nonExistPersonId))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("콘텐트 추가 (실패-개인 최대 프로젝트 개수 초과)")
	public void createContentEmailOverLimit() throws Exception {
		//Given
		Long personId = person.getId();
		Space space = person.getSpace();
		
		List<Content> newContents = new ArrayList<>();
		for(int i=0; i <= Space.MAX_CONTENT_NUM ; i++) {
			Content content = ContentTest.getContentInstance("title"+i);
			content.setSpace(space);
			space.getContents().add(content);
			newContents.add(content);
		}
		
		contentRepository.saveAll(newContents);
		
		//When
		mockMvc.perform(get("/space/makecontent/"+personId))
		
		//Then
				.andExpect(status().isBadRequest());
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("콘텐트 추가 (정상)")
	public void craeteContentTest() throws Exception{
		//Given
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
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("콘텐트 업데이트 (실패-입력된 id에 해당하는 content가 DB에 없을때)")
	public void updateContentNonExist() throws Exception{
		//Given
		Long nonExistContentId = ContentTest.getNonExistContentId(contentRepository);
		
		//When
		mockMvc.perform(get("/space/updatecontent/" + nonExistContentId))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles={"USER"})
	@Test
	@DisplayName("콘텐트 업데이트 (실패-해당 id의 콘텐트가 현재 접속자의 것이 아닐때)")
	public void updateContentWrongUser() throws Exception {
		//Given
		String anoterPersonEmail = "second@test.com";
		Person anotherPerson = PersonTest.getPersonInstance(anoterPersonEmail);
		personRepository.save(anotherPerson);
		
		Space anotherPersonsSpace = anotherPerson.getSpace();
		
		Content content = ContentTest.getContentInstance("not Mine");
		content.setSpace(anotherPersonsSpace);
		contentRepository.save(content);
		
		Long notMyContentId = content.getId();
		
		//When
		mockMvc.perform(get("/space/updatecontent/"+notMyContentId))
		
		//Then
				.andExpect(status().isBadRequest());
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@DisplayName("콘텐트 업데이트 (정상)")
	public void updateContentTest() throws Exception {
		//Given
		Space space = person.getSpace();
		Content content = ContentTest.getContentInstance();
		content.setSpace(space);
		
		contentRepository.save(content);
		
		Long contentId = content.getId();
		System.out.println(contentId);
		System.out.println(contentRepository.findById(contentId).get());
		
		content = contentRepository.findByIdWithSpaceAndPerson(contentId).get();
		
		//When
		mockMvc.perform(get("/space/updatecontent/" + contentId))
		
		//Then
				.andExpect(view().name("updateContent"));
		
	}
}
