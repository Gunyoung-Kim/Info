package com.gunyoung.info.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Link;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.repos.ContentRepository;
import com.gunyoung.info.repos.LinkRepository;
import com.gunyoung.info.repos.PersonRepository;
import com.gunyoung.info.repos.SpaceRepository;
import com.gunyoung.info.testutil.Integration;
import com.gunyoung.info.util.ContentTest;
import com.gunyoung.info.util.LinkTest;
import com.gunyoung.info.util.PersonTest;

/**
 * {@link PersonRestController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
@AutoConfigureMockMvc
class PersonRestControllerTest {
	
	@Autowired
	MockMvc mockMvc;

	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	SpaceRepository spaceRepository;
	
	@Autowired
	ContentRepository contentRepository;
	
	@Autowired
	LinkRepository linkRepository;

	private static final String MAIN_PERSON_EMAIL = "test@test.com";
	
	private Person person;
	
	@BeforeEach
	void setup() {
		person = PersonTest.getPersonInstance(MAIN_PERSON_EMAIL);
		personRepository.save(person);
	}
	
	@AfterEach
	void tearDown() {
		linkRepository.deleteAll();
		contentRepository.deleteAll();
		personRepository.deleteAll();
	}
	
	/*
	 *  - 대상 메소드:
	 *  	@GetMapping("/join/idverification")
	 *		String idVerification(@RequestParam("email") String email)
	 */
	
	@Test
	@DisplayName("Email 중복 여부 확인 (성공, 중복임)")
	void emailVerificationTestTrue() throws Exception {
		//Given
		String existEmail = person.getEmail();
		
		//When
		MvcResult result = mockMvc.perform(get("/join/emailverification")
				.param("email", existEmail))
				.andReturn();
		
		//Then
		assertEquals("true",result.getResponse().getContentAsString());
	}
	
	@Test
	@DisplayName("Email 중복 여부 확인 (성공, 중복아님)")
	void emailVerificationTestFalse() throws Exception {
		//Given
		String nonExistEMail = "nonexist@test.com";
		
		// 이메일 중복 안됨
		MvcResult result = mockMvc.perform(get("/join/emailverification")
				.param("email", nonExistEMail))
				.andReturn();
		
		assertEquals("false",result.getResponse().getContentAsString());
	}
	
	
	/*
	 *  - 대상 메소드: 
	 *  	@RequestMapping(value="/withdraw", method=RequestMethod.DELETE)
	 * 		ModelAndView personWithdraw(@RequestParam("email") String email,ModelAndView mav)
	 */
	
	@WithMockUser(username="none@google.com", roles= {"USER"})
	@Test
	@DisplayName("회원탈퇴 DELETE (실패-해당 계정이 DB에 존재하지 않을때)")
	void personWithdrawNonExist() throws Exception {
		//Given
		String nonExistEmail = "none@google.com";
		//When
		mockMvc.perform(delete("/withdraw")
				.param("email", nonExistEmail))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@DisplayName("회원탈퇴 DELETE (실패-로그인 계정이 탈퇴 계정과 일치하지 않을때")
	void personWithdrawNotMatch() throws Exception {
		//Given
		Person anotherPerson = PersonTest.getPersonInstance("second@test.com");
		personRepository.save(anotherPerson);
		
		String anotherPersonEmail = anotherPerson.getEmail();
		
		//When
		mockMvc.perform(delete("/withdraw")
				.param("email", anotherPersonEmail))
		
		//Then
				.andExpect(status().isBadRequest());
		
		assertTrue(personRepository.existsByEmail(anotherPersonEmail));
	}
	
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("회원탈퇴 DELETE (성공, Person 삭제 확인)")
	void personWithdrawTestCheckPerson() throws Exception {
		//Given
		
		//When
		mockMvc.perform(delete("/withdraw")
				.param("email", MAIN_PERSON_EMAIL))
		
		//Then
				.andExpect(status().isOk());

		assertFalse(personRepository.existsByEmail(MAIN_PERSON_EMAIL));
		
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("회원탈퇴 DELETE (성공, 관련 Space 삭제 확인)")
	void personWithdrawTestCheckSpace() throws Exception {
		//Given
		Space space = person.getSpace();
		Long spaceId = space.getId();
		
		//When
		mockMvc.perform(delete("/withdraw")
				.param("email", MAIN_PERSON_EMAIL))
		
		//Then
				.andExpect(status().isOk());

		assertFalse(spaceRepository.existsById(spaceId));
	}
	
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("회원탈퇴 DELETE (성공, 관련 Content 삭제 확인)")
	void personWithdrawTestCheckContent() throws Exception {
		//Given
		Space space = person.getSpace();
		Content content = ContentTest.getContentInstance("title");
		content.setSpace(space);
		contentRepository.save(content);
		Long contentId = content.getId();
			
		//When
		mockMvc.perform(delete("/withdraw")
				.param("email", MAIN_PERSON_EMAIL))
			
		//Then
				.andExpect(status().isOk());
		assertFalse(contentRepository.existsById(contentId));
	}
		
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("회원탈퇴 DELETE (성공, 관련 LINK 삭제 확인)")
	void personWithdrawTestCheckLink() throws Exception {
		//Given
		Space space = person.getSpace();
		Content content = ContentTest.getContentInstance("title");
		content.setSpace(space);
		contentRepository.save(content);
			
		Link link = LinkTest.getLinkInstance();
		link.setContent(content);
		linkRepository.save(link);
			
		Long linkId = link.getId();
			
		//When
		mockMvc.perform(delete("/withdraw")
				.param("email", MAIN_PERSON_EMAIL))
		
		//Then
				.andExpect(status().isOk());
		assertFalse(linkRepository.existsById(linkId));
	}	
}
