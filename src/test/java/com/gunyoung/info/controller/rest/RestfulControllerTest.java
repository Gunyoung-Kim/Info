package com.gunyoung.info.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.dto.MainListDTO;
import com.gunyoung.info.repos.PersonRepository;
import com.gunyoung.info.util.PersonTest;

/**
* {@link RestfulControllerTest} 에 대한 테스트 클래스
* 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
* MockMvc 활용을 통한 통합 테스트
* @author kimgun-yeong
*
*/
@SpringBootTest
@AutoConfigureMockMvc
public class RestfulControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	PersonRepository personRepository;

	private static final String MAIN_PERSON_EMAIL = "test@test.com";
	
	private Person person;
	
	@BeforeEach
	void setup() {
		person = PersonTest.getPersonInstance(MAIN_PERSON_EMAIL);
		personRepository.save(person);
	}
	
	@AfterEach
	void tearDown() {
		personRepository.deleteAll();
	}
	
	/*
	 *  - 대상 메소드:
	 *  	@GetMapping("/main/list")
	 *		public List<MainListDTO> index()
	 */
	
	@Test
	@DisplayName("메인 화면 리스트 반환 (성공)")
	public void indexTest() throws Exception {
		//Given
		long givenPersonNum = personRepository.count();
		
		//When
		MvcResult result = mockMvc.perform(get("/main/list"))
				
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		String responseString = result.getResponse().getContentAsString();
		
		ObjectMapper mapper = new ObjectMapper();
		List<MainListDTO> resultList = mapper.readValue(responseString, new TypeReference<List<MainListDTO>>() {});
		
		assertEquals(givenPersonNum, resultList.size());
	}
}
