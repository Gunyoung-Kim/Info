package com.gunyoung.info.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.repos.ContentRepository;
import com.gunyoung.info.repos.PersonRepository;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.services.domain.SpaceService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class ContentServiceTest {
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	ContentRepository contentRepository;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	ContentService contentService;
	
	@Autowired
	SpaceService spaceService;
	
	@BeforeEach
	void setup() {
		// 유저 등록
		if(!personService.existsByEmail("test@google.com")) {
			Person person = new Person();
			person.setEmail("test@google.com");
			person.setPassword("abcd1234");
			person.setFirstName("스트");
			person.setLastName("테");
										
			personService.save(person);
			
			// space 내용 설정
			Space space = person.getSpace();
			space.setDescription("test용 자기소개입니다.");
			space.setGithub("https://github.com/Gunyoung-Kim");
			
			// content 들 설정
			int contentsNumber = 1;
			for(int i=0;i<=contentsNumber;i++) {
				Content content = new Content();
				content.setTitle(i+" 번째 타이틀");
				content.setDescription(i+" 번째 프로젝트 설명");
				content.setContributors(i+" 번째 기여자들");
				content.setContents(i+ " 번째 프로젝트 내용");
				content.setSpace(space);
				contentService.save(content);
				
				space.getContents().add(content);
			}
			
			spaceService.save(space);
		}
		//2번쨰 유저 등록
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
	
	/*
	 *  - 대상 메소드:
	 *  	public Content save(Content content);
	 */
	
	@Test
	@Transactional
	@DisplayName("Content Save (성공)") 
	public void saveContentTest() {
		// 컨텐트 수정
		Person person = personRepository.findByEmail("test@google.com").get();
		Space space = person.getSpace();
		Content content = space.getContents().get(0);
		
		content.setTitle("changed Title");
		
		contentService.save(content);
		
		assertEquals(contentRepository.getById(content.getId()).getTitle(),"changed Title");
		
		// 컨텐트 추가
		
		long contentNum = contentRepository.count();
		
		content = new Content();
		
		content.setTitle("new Title");
		content.setDescription("new Des");
		content.setContributors("new Contri");
		content.setContents("new Contents");
		
		Long createdId = contentService.save(content).getId();
		
		assertEquals(contentNum+1,contentRepository.count());
		assertEquals(contentRepository.existsById(createdId),true);
	}
	
	/*
	 *  - 대상 메소드:
	 *  	public void deleteContent(Content content);
	 */
	
	@Test
	@Transactional
	@DisplayName("Content Delete (실패 - 존재하지 않음)")
	public void deleteContentNonExist() {
		long contentNum = contentRepository.count();
		Content content = new Content();
		
		content.setTitle("new Title");
		content.setDescription("new Des");
		content.setContributors("new Contri");
		content.setContents("new Contents");
		
		contentService.delete(content);
		
		assertEquals(contentNum, contentRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("Content Delete(성공)")
	public void deleteContentTest() {
		Content content = contentRepository.findAll().get(0);
		Space space = content.getSpace();
		int spaceContentNum = space.getContents().size();
		Long id = content.getId();
		
		contentService.delete(content);
		
		assertEquals(contentRepository.existsById(id),false);
		assertEquals(space.getContents().size(),spaceContentNum-1);
		
	}
	
	/*
	 *  - 대상 메소드:
	 *  	public void deleteContentById(Long id);
	 */
	
	@Test
	@Transactional
	@DisplayName("Content Delete By Id (실패- 존재하지 않음)")
	public void deleteContentByIdNonExist() {
		long contentNum = contentRepository.count();
		contentService.deleteById(Long.valueOf(100));
		
		assertEquals(contentNum, contentRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("Content Delete By Id (성공)")
	public void deleteContentByIdTest() {
		Content content = contentRepository.findAll().get(0);
		Space space = content.getSpace();
		int spaceContentNum = space.getContents().size();
		Long id = content.getId();
		
		contentService.deleteById(id);
		
		assertEquals(contentRepository.existsById(id),false);
		assertEquals(space.getContents().size(),spaceContentNum-1);
	}
}
