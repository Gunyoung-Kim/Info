package com.gunyoung.info.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Link;
import com.gunyoung.info.dto.LinkDTO;
import com.gunyoung.info.repos.ContentRepository;
import com.gunyoung.info.repos.LinkRepository;
import com.gunyoung.info.services.domain.LinkService;
import com.gunyoung.info.testutil.Integration;
import com.gunyoung.info.util.ContentTest;
import com.gunyoung.info.util.LinkTest;

/**
 * {@link LinkService} 에 대한 테스트 클래스 <br>
 * 테스트 범위:(통합 테스트) 서비스 계층 - 영속성 계층 
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
class LinkServiceTest {
	
	@Autowired
	LinkRepository linkRepository;
	
	@Autowired
	ContentRepository contentRepository;
	
	@Autowired
	LinkService linkService;
	
	private Link link;
	
	@BeforeEach
	void setup() {
		link = LinkTest.getLinkInstance();
		linkRepository.save(link);
	}
	
	@AfterEach
	void tearDown() {
		linkRepository.deleteAll();
	}
	
	/*
	 * Link findById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Link 찾기 -> 존재하지 않음")
	void findByIdTestNonExist() {
		//Given
		Long nonExistId = LinkTest.getNonExistLinkId(linkRepository);
		
		//When
		Link result = linkService.findById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 Link 찾기 -> 정상")
	void findByIdTest() {
		//Given
		Long linkId = link.getId();
		
		//When
		Link result = linkService.findById(linkId);
		
		//Then
		assertEquals(linkId, result.getId());
	}
	
	/*
	 * List<Link> findAllByContentId(Long contentId)
	 */
	
	@Test
	@Transactional
	@DisplayName("Content Id를 만족하는 모든 Link 반환 -> 정상")
	void findAllByContentIdTest() {
		//Given
		int newLinkNum = 10;
		addNewLinks(newLinkNum);
		
		Content content = ContentTest.getContentInstance();
		contentRepository.save(content);
		setContentToAllLinks(content);
		
		Long contentId = content.getId();
		long allLinkNum = linkRepository.count();
		
		//When
		List<Link> result = linkService.findAllByContentId(contentId);
		
		//Then
		verifyLinksNumAndThereContents(allLinkNum, content, result);
	}
	
	private void verifyLinksNumAndThereContents(long allLinksNum, Content content, List<Link> result) {
		assertEquals(allLinksNum, result.size());
		for(Link link: result) {
			assertEquals(content, link.getContent());
		}
	}
	
	/*
	 * Link save(Link link)
	 */
	
	@Test
	@DisplayName("Link 생성 및 수정 -> 수정, link url 수정") 
	void saveTestModifyURL() {
		//Given
		String changeURL = "https://change.com";
		link.setUrl(changeURL);
		
		Long linkId = link.getId();
		
		//When
		linkService.save(link);
		
		//Then
		assertEquals(changeURL, linkRepository.findById(linkId).get().getUrl());
	}
	
	@Test
	@DisplayName("Link 생성 및 수정 -> 새로운 Link 추가")
	void saveTestAddNewLink() {
		//Given
		Link newLink = LinkTest.getLinkInstance();
		
		long beforeLinkNum = linkRepository.count();
		
		//When
		linkService.save(newLink);
		
		//Then
		assertEquals(beforeLinkNum + 1 , linkRepository.count());
	}
	
	/*
	 * List<Link> updateLinksForContent(Content content, Iterable<LinkDTO> linkDTOs, Iterable<Link> existContentLinks)
	 */
	
	@Test
	@Transactional
	@DisplayName("기존의 Content의 Link들을 LinkDTO를 통해 업데이트 -> 정상, Content의 기존 Link 삭제확인")
	void updateLinksForContentTestCheckDeleteLink() {
		//Given
		Content content = getContentForLink();
		
		List<LinkDTO> emptyLinkDTOList = new ArrayList<>();
		
		Long linkId = link.getId();
		//When
		linkService.updateLinksForContent(content, emptyLinkDTOList);
		
		//Then
		assertFalse(linkRepository.existsById(linkId));
	}
	
	@Test
	@Transactional
	@DisplayName("기존의 Content의 Link들을 LinkDTO를 통해 업데이트 -> 정상, 기존의 Link 변경, URL 변경")
	void updateLinksForContentModifyLink() {
		//Given
		String changeURL = "https://change.com";
		Content content = getContentForLink();
		
		List<LinkDTO> linkDTOList = new ArrayList<>();
		
		LinkDTO linkDTO = LinkDTO.builder()
				.id(link.getId())
				.tag(link.getTag())
				.url(changeURL)
				.build();
		linkDTOList.add(linkDTO);
		
		Long linkId = link.getId();
		
		//When
		linkService.updateLinksForContent(content, linkDTOList);
		
		//Then
		assertEquals(changeURL, linkRepository.findById(linkId).get().getUrl());
	}
	
	@Test
	@Transactional
	@DisplayName("기존의 Content의 Link들을 LinkDTO를 통해 업데이트 -> 정상, 새로운 Link 추가")
	void updateLinksForContentTestAddLink() {
		//Given
		Content content = getContentForLink();
		String newLinkTag = "newTag";
		String newLinkURL = "newurl.com";
		
		List<LinkDTO> linkDTOList = new ArrayList<>();
		
		LinkDTO linkDTO = LinkDTO.builder()
				.tag(newLinkTag)
				.url(newLinkURL)
				.build();
		linkDTOList.add(linkDTO);
		
		long beforeLinkNum = linkRepository.count();
		
		//When
		linkService.updateLinksForContent(content, linkDTOList);
		
		//Then
		assertEquals(beforeLinkNum + 1, linkRepository.count() + 1);
	}
	
	private Content getContentForLink() {
		Content content = ContentTest.getContentInstance();
		contentRepository.save(content);
		
		link.setContent(content);
		content.getLinks().add(link);
		linkRepository.save(link);
		return content;
	}
	
	/*
	 * List<Link> saveAll(Iterable<Link> links)
	 */
	
	@Test
	@DisplayName("모든 Link들 저장 -> 정상")
	void saveAllTest() {
		//Given
		long beforeLinkNum = linkRepository.count();
		long newLinksNum = 6;
		List<Link> newLinks = getLinksByGivenNum(newLinksNum);
		
		//When
		linkService.saveAll(newLinks);
		
		//Then
		assertEquals(beforeLinkNum + newLinksNum,linkRepository.count());
	}
	
	private List<Link> getLinksByGivenNum(long newLinksNum) {
		List<Link> newLinks = new ArrayList<>();
		for(int i=0;i<newLinksNum;i++) {
			Link link = LinkTest.getLinkInstance();
			newLinks.add(link);
		}
		return newLinks;
	}
	
	/*
	 * void delete(Link link)
	 */
	
	@Test
	@DisplayName("Link 삭제 -> 정상")
	void deleteTest() {
		//Given
		Long deleteLinkId = link.getId();
		
		//When
		linkService.delete(link);
		
		//Then
		assertFalse(linkRepository.existsById(deleteLinkId));
	}
	
	/*
	 * void deleteById(Long id);
	 */
	
	@Test
	@DisplayName("ID로 Link 삭제 -> 정상")
	void deleteByIdTest() {
		//Given
		Long deleteLinkId = link.getId();
		
		//When
		linkService.deleteById(deleteLinkId);
		
		//Then
		assertFalse(linkRepository.existsById(deleteLinkId));
	}
	
	/*
	 * void deleteAllByContentId(Long contentId)
	 */
	
	@Test
	@DisplayName("Content ID로 Link들 삭제 -> 정상")
	void deleteAllByContentIdTest() {
		//Given
		int newLinkNum = 10;
		addNewLinks(newLinkNum);
		
		Content content = ContentTest.getContentInstance();
		contentRepository.save(content);
		setContentToAllLinks(content);
		
		//When
		linkService.deleteAllByContentId(content.getId());
		
		//Then
		assertEquals(0, linkRepository.count());
	}
	
	private void addNewLinks(int numOfNewLinks) {
		List<Link> links = new ArrayList<>();
		for(int i = 0; i < numOfNewLinks; i++) {
			Link link = LinkTest.getLinkInstance();
			links.add(link);
		}
		linkRepository.saveAll(links);
	}
	
	private void setContentToAllLinks(Content content) {
		List<Link> links = linkRepository.findAll();
		for(Link link: links) {
			link.setContent(content);
		}
		linkRepository.saveAll(links);
	}
}
