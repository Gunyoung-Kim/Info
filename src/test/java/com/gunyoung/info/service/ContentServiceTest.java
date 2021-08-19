package com.gunyoung.info.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.repos.ContentRepository;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.util.ContentTest;

/**
 * {@link ContentService} 에 대한 테스트 클래스 <br>
 * 테스트 범위:(통합 테스트) 서비스 계층 - 영속성 계층 
 * @author kimgun-yeong
 *
 */
@SpringBootTest
public class ContentServiceTest {
	
	@Autowired
	ContentService contentService;
	
	@Autowired
	ContentRepository contentRepository;
	
	private Content content;
	
	@BeforeEach
	void setup() {
		content = ContentTest.getContentInstance("title");
		contentRepository.save(content);
	}
	
	@AfterEach
	void tearDown() {
		contentRepository.deleteAll();
	}
	
	
	/*
	 *  - 대상 메소드:
	 *  	public Content save(Content content);
	 */
	
	@Test
	@DisplayName("Content Save (성공, 생성)") 
	public void addContentTest() {
		//Given
		long givenContentNum = contentRepository.count();
		String newContentTitle = "new title";
		Content newContent = ContentTest.getContentInstance(newContentTitle);
		
		//When
		contentService.save(newContent);
		
		//Then
		assertEquals(givenContentNum +1 , contentRepository.count());
	}
	
	@Test
	@DisplayName("Content Save (성공, 수정, 개수 변하지 않음 확인)") 
	public void modifyContentTestCount() {
		//Given
		long givenContentNum = contentRepository.count();
		String changeTitle = "changed Title";
		
		content.setTitle(changeTitle);
		
		//When
		contentService.save(content);
		
		//Then
		assertEquals(givenContentNum, contentRepository.count());
	}
	
	@Test
	@DisplayName("Content Save (성공, 수정, 변경 반영 확인)") 
	public void modifyContentTestNewTitle() {
		//Given
		String changeTitle = "changed Title";
		content.setTitle(changeTitle);
		
		Long contentId = content.getId();
		
		//When
		contentService.save(content);
		
		//Then
		assertEquals(changeTitle, contentRepository.findById(contentId).get().getTitle());
	}
	
	/*
	 *  - 대상 메소드:
	 *  	public void deleteContent(Content content);
	 */
	
	@Test
	@Transactional
	@DisplayName("Content Delete (실패 - 존재하지 않음)")
	public void deleteContentNonExist() {
		//Given
		long givenContentNum = contentRepository.count();
		Content nonExistContentInDB = ContentTest.getContentInstance("not in db");
		
		//When
		contentService.delete(nonExistContentInDB);
		
		//Then
		assertEquals(givenContentNum, contentRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("Content Delete(성공)")
	public void deleteContentTest() {
		//Given
		Long contentId = content.getId();
		
		//When
		contentService.delete(content);
		
		//Then
		assertFalse(contentRepository.existsById(contentId));
	}
	
	/*
	 *  - 대상 메소드:
	 *  	public void deleteContentById(Long id);
	 */
	
	@Test
	@Transactional
	@DisplayName("Content Delete By Id (실패- 존재하지 않음)")
	public void deleteContentByIdNonExist() {
		//Given
		long givenContentNum = contentRepository.count();
		Long nonExistContentId = ContentTest.getNonExistContentId(contentRepository);
		
		//When
		contentService.deleteById(nonExistContentId);
		
		//Then
		assertEquals(givenContentNum, contentRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("Content Delete By Id (성공)")
	public void deleteContentByIdTest() {
		//Given
		Long contentId = content.getId();
		
		//When
		contentService.deleteById(contentId);
		
		//Then
		assertFalse(contentRepository.existsById(contentId));
	}
}
