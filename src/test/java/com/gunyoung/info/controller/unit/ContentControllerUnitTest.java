package com.gunyoung.info.controller.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

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

import com.gunyoung.info.controller.ContentController;
import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.dto.ContentDTO;
import com.gunyoung.info.error.exceptions.access.NotMyResourceException;
import com.gunyoung.info.error.exceptions.exceed.ContentNumLimitExceedException;
import com.gunyoung.info.error.exceptions.nonexist.ContentNotFoundedException;
import com.gunyoung.info.error.exceptions.nonexist.PersonNotFoundedException;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.services.domain.LinkService;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.util.ContentTest;
import com.gunyoung.info.util.PersonTest;
import com.gunyoung.info.util.SpaceTest;

/**
 * {@link ContentController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ContentController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class ContentControllerUnitTest {
	
	@Mock
	PersonService personService;
	
	@Mock
	ContentService contentService;
	
	@Mock
	LinkService linkService;
	
	@InjectMocks
	ContentController contentController;
	
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
	 * public ModelAndView createMyContentView()
	 */
	
	@Test
	@DisplayName("세션 유저의 포트폴리오에 프로젝트 추가화면으로 리다이렉트 -> Session 에 저장된 email의 Person 없을 때")
	public void createMyContentViewTestPersonNonExist() {
		//Given
		String nonExistEmail = "nonexist@test.com";
		mockingAuthorityutilGetSessionUserEmail(nonExistEmail);
		given(personService.findByEmailWithSpace(nonExistEmail)).willReturn(null);
		
		//When
		ModelAndView result = contentController.createMyContentView();
		
		//Then
		assertEquals("redirect:/login", result.getViewName());
	}
	
	@Test
	@DisplayName("세션 유저의 포트폴리오에 프로젝트 추가화면으로 리다이렉트 -> 정상")
	public void createMyContentViewTestCheckMav() {
		//Given
		String loginUserEmail = "test@test.com";
		mockingAuthorityutilGetSessionUserEmail(loginUserEmail);
		Person person = mockingPersonServiceFindByEmailWithSpace(loginUserEmail);
		Long personId = person.getId();
		
		//When
		ModelAndView result = contentController.createMyContentView();
		
		//Then
		assertEquals("redirect:/space/makecontent/" + personId, result.getViewName());
	}
	
	
	/*
	 * public ModelAndView createContentView(@PathVariable Long personId,@ModelAttribute("formModel") Content content, ModelAndView mav)
	 */
	
	@Test
	@DisplayName("유저의 포트폴리오에 프로젝트 추가하는 페이지 반환 -> 해당 이메일의 유저가 없으면")
	public void createContentViewTestPersonNonExist() {
		//Given
		String nonExistEmail = "nonexist@test.com";
		mockingAuthorityutilGetSessionUserEmail(nonExistEmail);
		given(personService.findByEmailWithSpace(nonExistEmail)).willReturn(null);
		
		Long targetPersonId = Long.valueOf(52);
		
		//When, Then
		assertThrows(PersonNotFoundedException.class, () -> {
			contentController.createContentView(targetPersonId, null, mav);
		});
	}
	
	@Test
	@DisplayName("유저의 포트폴리오에 프로젝트 추가하는 페이지 반환 -> 로그인된 계정과 일치하지 않으면")
	public void createContentViewTestNotMyResource() {
		//Given
		String loginUserEmail = "test@test.com";
		mockingAuthorityutilGetSessionUserEmail(loginUserEmail);
		Person person = mockingPersonServiceFindByEmailWithSpace(loginUserEmail);
		
		Long targetPersonId = person.getId() + 2;
		
		//When, Then
		assertThrows(NotMyResourceException.class, () -> {
			contentController.createContentView(targetPersonId, null, mav);
		});
	}
	
	@Test
	@DisplayName("유저의 포트폴리오에 프로젝트 추가하는 페이지 반환 -> 개인에게 할당 된 최대 프로젝트 수 초과 시")
	public void createContentViewTestContentLimitExceeded() {
		//Given
		String loginUserEmail = "test@test.com";
		mockingAuthorityutilGetSessionUserEmail(loginUserEmail);
		Person person = mockingPersonServiceFindByEmailWithSpace(loginUserEmail);
		
		for(int i=0; i< Space.MAX_CONTENT_NUM; i++) {
			Content content = ContentTest.getContentInstance();
			person.getSpace().getContents().add(content);
		}
		
		Long personId = person.getId();
		
		//When, Then
		assertThrows(ContentNumLimitExceedException.class, () -> {
			contentController.createContentView(personId, null, mav);
		});
	}
	
	@Test
	@DisplayName("유저의 포트폴리오에 프로젝트 추가하는 페이지 반환 -> 정상, ModelAndView check")
	public void createContentViewTestCheckMav() {
		//Given
		String loginUserEmail = "test@test.com";
		mockingAuthorityutilGetSessionUserEmail(loginUserEmail);
		Person person = mockingPersonServiceFindByEmailWithSpace(loginUserEmail);
		
		Long personId = person.getId();
		Content content = ContentTest.getContentInstance();
		
		//When
		contentController.createContentView(personId, content, mav);
		
		//Then
		verifyModelAndViewInCreateContentViewTestCheckMav(content, personId);
	}
	
	private void verifyModelAndViewInCreateContentViewTestCheckMav(Content content, Long personId) {
		then(mav).should(times(1)).addObject("formModel", content);
		then(mav).should(times(1)).addObject("personId", personId);
		then(mav).should(times(1)).setViewName("createContent");
	}
	
	private Person mockingPersonServiceFindByEmailWithSpace(String personEmail) {
		Person person = PersonTest.getPersonInstance(personEmail);
		Long personId = Long.valueOf(52);
		person.setId(personId);
		given(personService.findByEmailWithSpace(personEmail)).willReturn(person);
		return person;
	}
	
	/*
	 * public ModelAndView updateContentView(@PathVariable Long contentId, @ModelAttribute("formModel") ContentDTO contentDto, ModelAndView mav)
	 */
	
	@Test
	@DisplayName("url에 입력된 id에 해당하는 콘텐츠의 정보 수정을 가능케하는 페이지 반환 -> 입력된 id에 해당하는 content가 DB 테이블에 없을때")
	public void updateContentViewTestContentNonExist() {
		//Given
		Long nonExistContentId = Long.valueOf(63);
		given(contentService.findByIdWithSpaceAndPerson(nonExistContentId)).willReturn(null);
		
		ContentDTO contentDTO = ContentTest.getContentDTOInstance();
		//When, Then
		assertThrows(ContentNotFoundedException.class, () -> {
			contentController.updateContentView(nonExistContentId, contentDTO, mav);
		});
	}
	
	@Test
	@DisplayName("url에 입력된 id에 해당하는 콘텐츠의 정보 수정을 가능케하는 페이지 반환 ->  현재 로그인 유저 != 해당 프로젝트 작성자")
	public void updateContentViewTestNotMyResource() {
		//Given
		Long contentId = Long.valueOf(53);
		Content content = mockingContentServiceFindByIdWithSpaceAndPerson(contentId);
		
		String hostEmail = PersonTest.DEFAULT_PERSON_EMAIL;
		Space space = SpaceTest.getSpaceInstance();
		space.setPerson(PersonTest.getPersonInstance(hostEmail));
		content.setSpace(space);
		
		String loginPersonEmail = "login@test.com";
		mockingAuthorityutilGetSessionUserEmail(loginPersonEmail);
		
		ContentDTO contentDTO = ContentTest.getContentDTOInstance();
		//When, Then
		assertThrows(NotMyResourceException.class, () -> {
			contentController.updateContentView(contentId, contentDTO, mav);
		});
	}
	
	@Test
	@DisplayName("url에 입력된 id에 해당하는 콘텐츠의 정보 수정을 가능케하는 페이지 반환 ->  정상, ModelAndView check")
	public void updateContentViewTestCheckMav() {
		//Given
		Long contentId = Long.valueOf(53);
		Content content = mockingContentServiceFindByIdWithSpaceAndPerson(contentId);
		
		String hostEmail = PersonTest.DEFAULT_PERSON_EMAIL;
		Space space = SpaceTest.getSpaceInstance();
		space.setPerson(PersonTest.getPersonInstance(hostEmail));
		content.setSpace(space);
		
		String loginPersonEmail = PersonTest.DEFAULT_PERSON_EMAIL;
		mockingAuthorityutilGetSessionUserEmail(loginPersonEmail);
		
		ContentDTO contentDTO = ContentTest.getContentDTOInstance();
		
		//When
		contentController.updateContentView(contentId, contentDTO, mav);
		
		//Then
		verifyModelAndViewForUpdateContentViewTestCheckMav(contentDTO, contentId);
	}
	
	private void verifyModelAndViewForUpdateContentViewTestCheckMav(ContentDTO contentDTO, Long contentId) {
		then(mav).should(times(1)).addObject("formModel", contentDTO);
		then(mav).should(times(1)).addObject("contentId", contentId);
		then(mav).should(times(1)).setViewName("updateContent");
	}
	
	private Content mockingContentServiceFindByIdWithSpaceAndPerson(Long contentId) {
		Content content = ContentTest.getContentInstance();
		content.setId(contentId);
		given(contentService.findByIdWithSpaceAndPerson(contentId)).willReturn(content);
		return content;
	}
	
	private void mockingAuthorityutilGetSessionUserEmail(String email) {
		given(securityContext.getAuthentication()).willReturn(auth);
		given(auth.getName()).willReturn(email);
	}
}
