package com.gunyoung.info.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Link;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.repos.ContentRepository;
import com.gunyoung.info.repos.LinkRepository;
import com.gunyoung.info.repos.SpaceRepository;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.testutil.Integration;
import com.gunyoung.info.util.ContentTest;
import com.gunyoung.info.util.LinkTest;
import com.gunyoung.info.util.SpaceTest;

/**
 * {@link ContentService} 에 대한 테스트 클래스 <br>
 * 테스트 범위:(통합 테스트) 서비스 계층 - 영속성 계층 
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
class ContentServiceTest {
	
	@Autowired
	ContentService contentService;
	
	@Autowired
	LinkRepository linkRepository;
	
	@Autowired
	SpaceRepository spaceRepository;
	
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
		linkRepository.deleteAll();
		contentRepository.deleteAll();
	}
	
	
	/*
	 *  - 대상 메소드:
	 *  	Content save(Content content);
	 */
	
	@Test
	@DisplayName("Content Save (성공, 생성)") 
	void addContentTest() {
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
	void modifyContentTestCount() {
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
	void modifyContentTestNewTitle() {
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
	 *  	void deleteContent(Content content);
	 */
	
	@Test
	@DisplayName("Content Delete (실패 - 존재하지 않음)")
	void deleteContentNonExist() {
		//Given
		long givenContentNum = contentRepository.count();
		Content nonExistContentInDB = ContentTest.getContentInstance("not in db");
		
		//When
		contentService.delete(nonExistContentInDB);
		
		//Then
		assertEquals(givenContentNum, contentRepository.count());
	}
	
	@Test
	@DisplayName("Content Delete (성공), Content 삭제 확인")
	void deleteContentTestCheckContentRemove() {
		//Given
		Long contentId = content.getId();
		
		//When
		contentService.delete(content);
		
		//Then
		assertFalse(contentRepository.existsById(contentId));
	}
	
	@Test
	@DisplayName("Content Delete (성공), 관련 Link들 삭제")
	void deleteContentTestCheckLinksRemove() {
		//Given
		int numOfLinksForContent = 8;
		setLinksForContent(content, numOfLinksForContent);
		
		long beforeLinkNum = linkRepository.count();
		
		//When
		contentService.delete(content);
		
		//Then
		assertEquals(beforeLinkNum - numOfLinksForContent, linkRepository.count());
	}
	
	/*
	 *  - 대상 메소드:
	 *  	void deleteContentById(Long id);
	 */
	
	@Test
	@DisplayName("Content Delete By Id (실패- 존재하지 않음)")
	void deleteContentByIdNonExist() {
		//Given
		long givenContentNum = contentRepository.count();
		Long nonExistContentId = ContentTest.getNonExistContentId(contentRepository);
		
		//When
		contentService.deleteById(nonExistContentId);
		
		//Then
		assertEquals(givenContentNum, contentRepository.count());
	}
	
	@Test
	@DisplayName("Content Delete By Id (성공), Check Content Remove")
	void deleteContentByIdTestCheckContentRemove() {
		//Given
		Long contentId = content.getId();
		
		//When
		contentService.deleteById(contentId);
		
		//Then
		assertFalse(contentRepository.existsById(contentId));
	}
	
	@Test
	@DisplayName("Content Delete By Id (성공), Check Links Remove")
	void deleteContentByIdTestCheckLinksRemove() {
		//Given
		int numOfLinksForContent = 8;
		setLinksForContent(content, numOfLinksForContent);
		Long contentId = content.getId();
		long beforeLinkNum = linkRepository.count();
		
		//When
		contentService.deleteById(contentId);
		
		//Then
		assertEquals(beforeLinkNum - numOfLinksForContent, linkRepository.count());
	}
	
	/*
	 * void deleteAllBySpaceId(Long spaceId)
	 */
	
	@Test
	@DisplayName("Space ID로 Content들 삭제 -> 정상, Check Contents Remove")
	void deleteAllBySpaceIdTestCheckContentsRemove() {
		//Given
		int newContentsNum = 9;
		addNewContents(newContentsNum);
		
		Space space = SpaceTest.getSpaceInstance();
		spaceRepository.save(space);
		setSpaceForAllContents(space);
		
		//When
		contentService.deleteAllBySpaceId(space.getId());
		
		//Then
		assertEquals(0, contentRepository.count());
	}
	
	@Test
	@DisplayName("Space ID로 Content들 삭제 -> 정상, Check Links Remove")
	void deleteAllBySpaceIdTestCheckLinksRemove() {
		//Given
		int newContentsNum = 3;
		addNewContents(newContentsNum);
		
		Space space = SpaceTest.getSpaceInstance();
		spaceRepository.save(space);
		setSpaceForAllContents(space);
		
		long numOfAllContent = contentRepository.count();
		int numOfLinksForContent = 2;
		setLinksForAllContent(numOfLinksForContent);
		
		long beforeLinkNum = linkRepository.count();
		
		//When
		contentService.deleteAllBySpaceId(space.getId());
		
		//Then
		assertEquals(beforeLinkNum - numOfAllContent * numOfLinksForContent, linkRepository.count());
	}
	
	private void setLinksForAllContent(int numOfLinksForContent) {
		List<Content> contents = contentRepository.findAll();
		for(Content content: contents) {
			setLinksForContent(content, numOfLinksForContent);
		}
	}
	
	private void addNewContents(int newContentsNum) {
		List<Content> contents = new ArrayList<>();
		for(int i = 0; i < newContentsNum; i++) {
			Content content = ContentTest.getContentInstance();
			contents.add(content);
		}
		contentRepository.saveAll(contents);
	}
	
	private void setSpaceForAllContents(Space space) {
		List<Content> contents = contentRepository.findAll();
		for(Content content: contents) {
			content.setSpace(space);
		}
		contentRepository.saveAll(contents);
	}
	
	private void setLinksForContent(Content content, int numOfLinksForContent) {
		List<Link> links = new ArrayList<>();
		for(int i = 0; i < numOfLinksForContent; i++) {
			Link link = LinkTest.getLinkInstance();
			link.setContent(content);
			links.add(link);
		}
		linkRepository.saveAll(links);
	}
}
