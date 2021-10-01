package com.gunyoung.info.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.repos.ContentRepository;
import com.gunyoung.info.repos.PersonRepository;
import com.gunyoung.info.repos.SpaceRepository;
import com.gunyoung.info.testutil.Integration;
import com.gunyoung.info.util.PersonTest;

/**
 * {@link SpaceController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
@AutoConfigureMockMvc
public class SpaceControllerTest {
	
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
	 *  	@RequestMapping(value="/space", method= RequestMethod.GET)
	 * 		public ModelAndView myspaceView(ModelAndView mav)
	 */
	
	@WithAnonymousUser
	@Test
	@DisplayName("내 포트폴리오 열람 (정상-로그인 안되있을때)") 
	public void myspaceViewAnonymousTest() throws Exception {
		//When
		mockMvc.perform(get("/space"))
		
		//Then
				.andExpect(status().is(302));
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"})
	@Test
	@DisplayName("내 포트폴리오 열람 (정상-로그인 되어있을때)")
	public void mySpaceViewUserTest() throws Exception {
		//Given
		Long personId = person.getId();
		
		//When
		mockMvc.perform(get("/space"))
		
		//Then
				.andExpect(redirectedUrl("/space/"+ personId));
	}
	
	/*
	 *  - 대상 메소드:
	 *  	@RequestMapping(value="/space/{userId}", method= RequestMethod.GET)
	 *  	public ModelAndView spaceView(@PathVariable Long userId, ModelAndView mav)
	 */
	
	@WithAnonymousUser
	@Test
	@DisplayName("포트폴리오 열람 (실패-해당 Id의 Person DB에 없을때)")
	public void spaceEmailNonExists() throws Exception {
		//Given
		Long nonExistPersonId = PersonTest.getNonExistPersonId(personRepository);
		
		//When
		mockMvc.perform(get("/space/" + nonExistPersonId))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(username="second@naver.com",roles = {"USER"}) //anonymous 로 해도 동
	@Test
	@DisplayName("포트폴리오 열람 (성공-접속자가 해당 포트폴리오 주인이 아닐때)")
	public void spaceNotHostTest() throws Exception {
		//Given
		Person host = person;
		Long hostId = host.getId();
		
		//When
		mockMvc.perform(get("/space/" + hostId))
		
		//Then
			.andExpect(view().name("portfolio"))
			.andExpect(model().attribute("isHost", false));
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles = {"USER"}) 
	@Test
	@DisplayName("포트폴리오 열람 (성공-접속자가 해당 포트폴리오 주인일때)")
	public void spaceTest() throws Exception {
		//Given
		Long personId = person.getId();
		
		//When
		mockMvc.perform(get("/space/" + personId))
		
		//Then
				.andExpect(view().name("portfolio"))
				.andExpect(model().attribute("isHost", true));
	}
	
	/*
	 *  - 대상 메소드:
	 *  	@RequestMapping(value="/space/updateprofile", method = RequestMethod.GET)
	 *		public ModelAndView updateProfileView(@ModelAttribute("formModel") ProfileDTO profileDTO, ModelAndView mav)
	 */
	
	@WithMockUser(username="none@none.com", roles= {"USER"})
	@Test
	@DisplayName("프로필 업데이트 (실패-현재 로그인 유저의 이메일이 DB에 없을때)")
	public void updateProfileEmailNonExist() throws Exception {
		//When
		mockMvc.perform(get("/space/updateprofile"))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithAnonymousUser
	@Test
	@DisplayName("프로필 업데이트 (성공-로그인 되어 있지 않을 때)")
	public void updateProfileAnonymousTest() throws Exception {
		//When
		mockMvc.perform(get("/space/updateprofile"))
		
		//Then
				.andExpect(status().is(302));
	}
	
	@WithMockUser(username=MAIN_PERSON_EMAIL, roles= {"USER"}) 
	@Test
	@DisplayName("프로필 업데이트 (성공-로그인 되어 있을때)")
	public void updateProfileTest() throws Exception {
		//When
		mockMvc.perform(get("/space/updateprofile"))
		
		//Then
				.andExpect(view().name("updateProfile"));
	}
}
