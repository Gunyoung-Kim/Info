package com.gunyoung.info.controller;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.dto.MainListObject;
import com.gunyoung.info.services.ContentService;
import com.gunyoung.info.services.PersonService;
import com.gunyoung.info.services.SpaceService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
public class RestfulControllerTest {
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
						
							
			// space 내용 설정
			Space space = person.getSpace();
			space.setDescription("test용 자기소개입니다.");
			space.setGithub("https://github.com/Gunyoung-Kim");
					
			personService.save(person);
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
	
	/*
	 *  - 대상 메소드:
	 *  	@GetMapping("/main/list")
	 *		public List<MainListObject> index()
	 */
	
	@Test
	@DisplayName("메인 화면 리스트 반환 (성공)")
	public void indexTest() throws Exception {
		long personNum = personService.countAll();
		MvcResult result = mockMvc.perform(get("/main/list"))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseString = result.getResponse().getContentAsString();
		
		ObjectMapper mapper = new ObjectMapper();
		
		List<MainListObject> resultList = mapper.readValue(responseString, new TypeReference<List<MainListObject>>() {});
		
		assertEquals(resultList.size(), personNum);
	}
	
	/*
	 *  - 대상 메소드:
	 *  	@GetMapping("/join/idverification")
	 *		public String idVerification(@RequestParam("email") String email)
	 */
	
	@Test
	@DisplayName("Email 중복 여부 확인 (성공)")
	public void emailVerification() throws Exception {
		// 이메일 중복됨
		MvcResult result = mockMvc.perform(get("/join/emailverification").param("email", "test@google.com")).andReturn();
		
		assertEquals("true",result.getResponse().getContentAsString());
		
		// 이메일 중복 안됨
		result = mockMvc.perform(get("/join/emailverification").param("email", "none@google.com")).andReturn();
		
		assertEquals("false",result.getResponse().getContentAsString());
	}
}
