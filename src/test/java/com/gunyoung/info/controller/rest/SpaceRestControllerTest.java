package com.gunyoung.info.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.repos.ContentRepository;
import com.gunyoung.info.repos.PersonRepository;
import com.gunyoung.info.repos.SpaceRepository;
import com.gunyoung.info.util.PersonTest;

/**
 * {@link SpaceRestControllerTest} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SpaceRestControllerTest {
	
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
	 *  	@RequestMapping(value="/space/updateprofile", method = RequestMethod.PUT)
	 * 		public void updateProfilePost(@ModelAttribute("formModel") @Valid ProfileDTO profileDTO)
	 */
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Transactional
	@Test
	@DisplayName("프로필 업데이트 (실패-유효성 검사 불통과)")
	public void updateProfilePostNonValidate() throws Exception {
		//Given
		String givenPersonFirstName = person.getFirstName();
		MultiValueMap<String, String> paramMap = getUpdateProfileFailByEmptyFirstNameParamMap();
		
		//When
		mockMvc.perform(put("/space/updateprofile")
				.params(paramMap))
		
		//Then
				.andExpect(status().is4xxClientError());
		
		Person afterTestPerson = personRepository.findByEmail(MAIN_PERSON_EMAIL).get();
		assertEquals(givenPersonFirstName, afterTestPerson.getFirstName());
	}
	
	private MultiValueMap<String, String> getUpdateProfileFailByEmptyFirstNameParamMap() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("email", MAIN_PERSON_EMAIL);
		map.add("firstName", ""); // 이름을 일부러 비움
		map.add("lastName", "구");
		map.add("description", "changed description");
		map.add("github","changed github");
		map.add("instagram", "changed instagram");
		map.add("tweeter", "changed tweeter");
		map.add("facebook", "changed facebook");
		
		return map;
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("프로필 업데이트 (성공)")
	public void updateProfilePostTest() throws Exception {
		//Given
		String changedFirstName = "변화";
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("email", MAIN_PERSON_EMAIL);
		map.add("firstName", changedFirstName);
		map.add("lastName", "구");
		map.add("description", "changed description");
		map.add("github","changed github");
		map.add("instagram", "changed instagram");
		map.add("tweeter", "changed tweeter");
		map.add("facebook", "changed facebook");
		
		//When
		mockMvc.perform(put("/space/updateprofile")
				.params(map))
		
		//Then
				.andExpect(status().isOk());
		
		Person afterTestPerson = personRepository.findByEmail(MAIN_PERSON_EMAIL).get();
		assertEquals(changedFirstName, afterTestPerson.getFirstName());
	}
}
