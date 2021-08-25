package com.gunyoung.info.controller.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.info.controller.SpaceController;
import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.error.exceptions.nonexist.PersonNotFoundedException;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.util.PersonTest;

/**
 * {@link SpaceController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) SpaceController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class SpaceControllerUnitTest {
	
	@Mock
	PersonService personService;
	
	@Mock
	ContentService contentService;
	
	@InjectMocks
	SpaceController spaceController;
	
	private ModelAndView mav;
	
	private SecurityContext securityContext;
	
	private Authentication auth;
	
	@BeforeEach
	void setup() {
		mav = mock(ModelAndView.class);
		
		securityContext = mock(SecurityContext.class);
		SecurityContextHolder.setContext(securityContext);
		
		auth = mock(Authentication.class);
	}
	
	/*
	 * public ModelAndView myspaceView()
	 */
	
	@Test
	@DisplayName("현재 로그인되있는 사용자 본인의 포트폴리오 페이지 반환 -> 세션 email의 Person 없음")
	public void myspaceViewTestPersonNonExist() {
		//Given
		String nonExistPersonEmail = "nonexist@test.com";
		mockingAuthorityUtilGetSessionUserEmail(nonExistPersonEmail);
		given(personService.findByEmail(nonExistPersonEmail)).willReturn(null);
		
		//When
		ModelAndView result = spaceController.myspaceView();
		
		//Then
		assertEquals("redirect:/login", result.getViewName());
	}
	
	@Test
	@DisplayName("현재 로그인되있는 사용자 본인의 포트폴리오 페이지 반환 -> 정상")
	public void myspaceViewTestCheckViewName() {
		//Given
		String sessionPersonEmail = PersonTest.DEFAULT_PERSON_EMAIL;
		mockingAuthorityUtilGetSessionUserEmail(sessionPersonEmail);
		
		Person person = PersonTest.getPersonInstance(sessionPersonEmail);
		given(personService.findByEmail(sessionPersonEmail)).willReturn(person);
		
		Long personId = Long.valueOf(25);
		person.setId(personId);
		
		//When
		ModelAndView result = spaceController.myspaceView();
		
		//Then
		assertEquals("redirect:/space/" + personId, result.getViewName());
	}

	/*
	 * public ModelAndView spaceView(@PathVariable Long userId, ModelAndView mav)
	 */
	
	@Test
	@DisplayName("개인 포트폴리오 페이지 반환 -> 존재하지 않는 Person ID")
	public void spaceViewTestPersonNonExist() {
		//Given
		Long nonExistPersonId = Long.valueOf(88);
		given(personService.findByIdWithSpace(nonExistPersonId)).willReturn(null);
		
		//When ,Then
		assertThrows(PersonNotFoundedException.class, () -> {
			spaceController.spaceView(nonExistPersonId, mav);
		});
	}
	
	@Test
	@DisplayName("개인 포트폴리오 페이지 반환 -> 정상, 주인이 요청, ModelAndView check")
	public void spaceViewForHostTestCheckMav() {
		//Given
		Long personId = Long.valueOf(62);
		Person person = mockingPersonServiceFindByIdWithSpace(personId);
		
		Long spaceId = Long.valueOf(82);
		person.getSpace().setId(spaceId);
		
		List<Content> givenContents = new ArrayList<>();
		given(contentService.findAllBySpaceIdWithLinks(spaceId)).willReturn(givenContents);
		
		mockingAuthorityUtilGetSessionUserEmail(person.getEmail());
		
		//When
		spaceController.spaceView(personId, mav);
		
		//Then
		verifyModelAndViewForSpaceViewForHostTestCheckMav(givenContents);
	}
	
	private void verifyModelAndViewForSpaceViewForHostTestCheckMav(List<Content> givenContents) {
		then(mav).should(times(1)).addObject("contents", givenContents);
		then(mav).should(times(1)).addObject("isHost", true);
		then(mav).should(times(1)).setViewName("portfolio");
	}
	
	private Person mockingPersonServiceFindByIdWithSpace(Long personId) {
		Person person = PersonTest.getPersonInstance();
		person.setId(personId);
		given(personService.findByIdWithSpace(personId)).willReturn(person);
		return person;
	}
	
	/*
	 * public ModelAndView updateProfileView(ModelAndView mav)
	 */
	
	@Test
	@DisplayName("현재 로그인한 유저의 프로필을 변경하기 위한 뷰를 반환 -> 현재 로그인된 유저의 이메일이 DB에 없으면 ")
	public void updateProfileViewTestPersonNonExist() {
		//Given
		String nonExistPersonEmail = "nonexist@test.com";
		mockingAuthorityUtilGetSessionUserEmail(nonExistPersonEmail);
		
		given(personService.findByEmailWithSpace(nonExistPersonEmail)).willReturn(null);
		
		//When, Then
		assertThrows(PersonNotFoundedException.class, () -> {
			spaceController.updateProfileView(mav);
		});
	}
	
	@Test
	@DisplayName("현재 로그인한 유저의 프로필을 변경하기 위한 뷰를 반환 -> 정상, Check ModelandView")
	public void updateProfileViewTestCheckMav() {
		//Given
		String personEmail = "test@test.com";
		mockingAuthorityUtilGetSessionUserEmail(personEmail);
		mockingPersonServiceFindByEmailWithSpace(personEmail);
		
		//When
		spaceController.updateProfileView(mav);
		
		//Then
		then(mav).should(times(1)).setViewName("updateProfile");
	}
	
	private Person mockingPersonServiceFindByEmailWithSpace(String email) {
		Person person = PersonTest.getPersonInstance(email);
		given(personService.findByEmailWithSpace(email)).willReturn(person);
		return person;
	}
	
	private void mockingAuthorityUtilGetSessionUserEmail(String email) {
		given(securityContext.getAuthentication()).willReturn(auth);
		given(auth.getName()).willReturn(email);
	}
}
