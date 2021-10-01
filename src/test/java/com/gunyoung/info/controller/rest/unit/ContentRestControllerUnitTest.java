package com.gunyoung.info.controller.rest.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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

import com.gunyoung.info.controller.rest.ContentRestController;
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
 * {@link ContentRestController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ContentRestController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class ContentRestControllerUnitTest {
	
	@Mock
	ContentService contentService;
	
	@Mock
	PersonService personService;
	
	@Mock
	LinkService linkService;
	
	@InjectMocks
	ContentRestController contentRestController;
	
	private ContentDTO contentDTO;
	
	private SecurityContext securityContext;
	
	private Authentication auth;
	
	@BeforeEach
	void setup() {
		contentDTO = ContentTest.getContentDTOInstance();
		securityContext = mock(SecurityContext.class);
		SecurityContextHolder.setContext(securityContext);
		
		auth = mock(Authentication.class);
	}
	
	/*
	 * public void createContent(@PathVariable Long personId, @Valid @ModelAttribute ContentDTO contentDTO)
	 */
	
	@Test
	@DisplayName("프로젝트 추가 처리 -> 로그인 이메일의 Person 존재하지 않을때")
	public void createContentTestPersonNonExist() {
		//Given
		String nonExistEmail = "nonexist@test.com";
		mockingAuthorityutilGetSessionUserEmail(nonExistEmail);
		given(personService.findByEmailWithSpace(nonExistEmail)).willReturn(null);
		
		ContentDTO contentDTO = ContentTest.getContentDTOInstance();
		Long personId = Long.valueOf(24);
		//When, Then
		assertThrows(PersonNotFoundedException.class, () -> {
			contentRestController.createContent(personId, contentDTO);
		});
	}
	
	@Test
	@DisplayName("프로젝트 추가 처리 -> 로그인된 정보가 해당 포트폴리오 주인이 아닐때")
	public void createContentTestNotMyResource() {
		//Given
		String loginPersonEmail = "test@test.com";
		mockingAuthorityutilGetSessionUserEmail(loginPersonEmail);
		
		Person loginPerson = mockingPersonServiceFindByEmailWithSpace(loginPersonEmail);
		
		Long loginPersonId = Long.valueOf(244);
		loginPerson.setId(loginPersonId);
		Long targetPersonId = Long.valueOf(99);
		
		//When, Then
		assertThrows(NotMyResourceException.class, () -> {
			contentRestController.createContent(targetPersonId, contentDTO);
		});
	}
	
	@Test
	@DisplayName("프로젝트 추가 처리 -> 개인 최대 프로젝트 개수 초과")
	public void createContentTestContentNumLimitExceeded() {
		//Given
		String loginPersonEmail = "test@test.com";
		mockingAuthorityutilGetSessionUserEmail(loginPersonEmail);
		Person loginPerson = mockingPersonServiceFindByEmailWithSpace(loginPersonEmail);
		
		for(int i=0; i< Space.MAX_CONTENT_NUM; i++) {
			Content content = ContentTest.getContentInstance();
			loginPerson.getSpace().getContents().add(content);
		}
		
		//When, Then
		assertThrows(ContentNumLimitExceedException.class, () -> {
			contentRestController.createContent(loginPerson.getId(), contentDTO);
		});
	}
	
	@Test
	@DisplayName("프로젝트 추가 처리 -> 정상, check services")
	public void createContentTestCheckServices() {
		//Given
		String loginPersonEmail = "test@test.com";
		mockingAuthorityutilGetSessionUserEmail(loginPersonEmail);
		Person loginPerson = mockingPersonServiceFindByEmailWithSpace(loginPersonEmail);
		
		//When
		contentRestController.createContent(loginPerson.getId(), contentDTO);
		
		//Then
		verifyContentServiceAndLinkService_createContentTestCheckServices();
	}
	
	private void verifyContentServiceAndLinkService_createContentTestCheckServices() {
		then(contentService).should(times(1)).save(any(Content.class));
		then(linkService).should(times(1)).saveAll(anyList());
	}
	
	private Person mockingPersonServiceFindByEmailWithSpace(String personEmail) {
		Person person = PersonTest.getPersonInstance(personEmail);
		Long personId = Long.valueOf(52);
		person.setId(personId);
		given(personService.findByEmailWithSpace(personEmail)).willReturn(person);
		return person;
	}
	
	/*
	 * public void deleteContent(@PathVariable long id)
	 */
	
	@Test
	@DisplayName("url에 명시된 id의 콘텐트 삭제 -> 입력된 id에 해당하는 content가 DB 테이블에 없을때")
	public void deleteContentTestContentNonExist() {
		//Given
		Long nonExistContentId = Long.valueOf(25);
		given(contentService.findByIdWithSpaceAndPerson(nonExistContentId)).willReturn(null);
		
		//When, Then
		assertThrows(ContentNotFoundedException.class, () -> {
			contentRestController.deleteContent(nonExistContentId);
		});
	}
	
	@Test
	@DisplayName("url에 명시된 id의 콘텐트 삭제 -> 현재 로그인 유저 != 해당 프로젝트 작성자")
	public void deleteContentTestNoMyResource() {
		//Given
		Long contentId = Long.valueOf(53);
		Content content = mockingContentServiceFindByIdWithSpaceAndPerson(contentId);
		
		String loginPersonEmail = "test@test.com";
		mockingAuthorityutilGetSessionUserEmail(loginPersonEmail);
		
		String contentHostEmail = "notyours@test.com";
		Person host = PersonTest.getPersonInstance(contentHostEmail);
		content.getSpace().setPerson(host);
		
		//When, Then
		assertThrows(NotMyResourceException.class, () -> {
			contentRestController.deleteContent(contentId);
		});
	}
	
	@Test
	@DisplayName("url에 명시된 id의 콘텐트 삭제 -> 정상, contentService delete check")
	public void deleteContentTestCheckContentServiceDelete() {
		//Given
		Long contentId = Long.valueOf(53);
		Content content = mockingContentServiceFindByIdWithSpaceAndPerson(contentId);
		
		String loginPersonEmail = "test@test.com";
		mockingAuthorityutilGetSessionUserEmail(loginPersonEmail);
		
		Person loginPerson = PersonTest.getPersonInstance(loginPersonEmail);
		content.getSpace().setPerson(loginPerson);
		
		//When
		contentRestController.deleteContent(contentId);
		
		//Then
		then(contentService).should(times(1)).delete(content);
	}
	
	private Content mockingContentServiceFindByIdWithSpaceAndPerson(Long contentId) {
		Content content = ContentTest.getContentInstance();
		content.setId(contentId);
		
		Space space = SpaceTest.getSpaceInstance();
		content.setSpace(space);
		given(contentService.findByIdWithSpaceAndPerson(contentId)).willReturn(content);
		return content;
	}
	
	/*
	 * public void updateContent(@PathVariable Long id, @ModelAttribute ContentDTO contentDto)
	 */
	
	@Test
	@DisplayName("content 수정 처리 -> 입력된 id에 해당하는 content가 DB 테이블에 없을때")
	public void updateContentTestContentNonExist() {
		//Given
		Long nonExistContentId = Long.valueOf(52);
		given(contentService.findByIdWithLinks(nonExistContentId)).willReturn(null);
		
		//When, Then
		assertThrows(ContentNotFoundedException.class, () -> {
			contentRestController.updateContent(nonExistContentId, contentDTO);
		});
	}
	
	@Test
	@DisplayName("content 수정 처리 -> 폼에 보내진 hostId에 해당하는 Person 없을때")
	public void updateContentTestPersonNonExist() {
		//Given
		Long contentId = Long.valueOf(52);
		mockingContentServiceFindByIdWithLinks(contentId);
		
		Long contentHostId = Long.valueOf(28);
		contentDTO.setHostId(contentHostId);
		given(personService.findById(contentHostId)).willReturn(null);
		
		//When, Then
		assertThrows(PersonNotFoundedException.class, () -> {
			contentRestController.updateContent(contentId, contentDTO);
		});
	}
	
	@Test
	@DisplayName("content 수정 처리 -> 현재 로그인 유저 != 해당 프로젝트 작성자")
	public void updateContentTestNotMyResource() {
		//Given
		Long contentId = Long.valueOf(52);
		mockingContentServiceFindByIdWithLinks(contentId);
		
		Long contentHostId = Long.valueOf(63);
		Person host = mockingPersonServiceFindByIdAndSetContentDTOHostID(contentHostId);
		
		String loginPersonEmail = "notmatch" + host.getEmail();
		mockingAuthorityutilGetSessionUserEmail(loginPersonEmail);
		
		//When, Then
		assertThrows(NotMyResourceException.class, () -> {
			contentRestController.updateContent(contentId, contentDTO);
		});
	}
	
	@Test
	@DisplayName("content 수정 처리 -> 정상, sevices check")
	public void updateContentTestCheckServices() {
		//Given
		Long contentId = Long.valueOf(52);
		Content content = mockingContentServiceFindByIdWithLinks(contentId);
		
		Long contentHostId = Long.valueOf(63);
		Person host = mockingPersonServiceFindByIdAndSetContentDTOHostID(contentHostId);
		
		mockingAuthorityutilGetSessionUserEmail(host.getEmail());
		//When
		contentRestController.updateContent(contentId, contentDTO);
		
		//Then
		then(contentService).should(times(1)).save(content);
		then(linkService).should(times(1)).updateLinksForContent(content, contentDTO.getLinks());
	}
	
	private Content mockingContentServiceFindByIdWithLinks(Long contentId) {
		Content content = ContentTest.getContentInstance();
		given(contentService.findByIdWithLinks(contentId)).willReturn(content);
		return content;
	}
	
	private Person mockingPersonServiceFindByIdAndSetContentDTOHostID(Long contentHostId) {
		Person person = PersonTest.getPersonInstance();
		given(personService.findById(contentHostId)).willReturn(person);
		contentDTO.setHostId(contentHostId);
		return person;
	}
 	
	private void mockingAuthorityutilGetSessionUserEmail(String email) {
		given(securityContext.getAuthentication()).willReturn(auth);
		given(auth.getName()).willReturn(email);
	}
}
