package com.gunyoung.info.service.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.repos.ContentRepository;
import com.gunyoung.info.services.domain.ContentServiceImpl;
import com.gunyoung.info.services.domain.LinkService;
import com.gunyoung.info.util.ContentTest;

/**
 * {@link ContentServiceImpl}에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) Service class only <br>
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class ContentServiceUnitTest {
	
	@Mock
	ContentRepository contentRepository;
	
	@Mock
	LinkService linkService;
	
	@InjectMocks
	ContentServiceImpl contentService;
	
	private Content content; 
	
	@BeforeEach
	void setup() {
		content = ContentTest.getContentInstance();
	}
	
	/*
	 * Content findById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Content 찾기 -> 존재하지 않음")
	void findByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(contentRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		Content result = contentService.findById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 Content 찾기 -> 정상")
	void findByIdTest() {
		//Given
		Long contentId = Long.valueOf(1);
		
		given(contentRepository.findById(contentId)).willReturn(Optional.of(content));
		
		//When
		Content result = contentService.findById(contentId);
		
		//Then
		assertEquals(content, result);
	}
	
	/*
	 * Content findByIdWithSpaceAndPerson(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Content 찾기, Space Person 페치조인  -> 존재하지 않음")
	void findByIdWithSpaceAndPersonNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(contentRepository.findByIdWithSpaceAndPerson(nonExistId)).willReturn(Optional.empty());
		
		//When
		Content result = contentService.findByIdWithSpaceAndPerson(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 Content 찾기, Space Person 페치조인 -> 정상")
	void findByIdWithSpaceAndPersonTest() {
		//Given
		Long contentId = Long.valueOf(1);
		
		given(contentRepository.findByIdWithSpaceAndPerson(contentId)).willReturn(Optional.of(content));
		
		//When
		Content result = contentService.findByIdWithSpaceAndPerson(contentId);
		
		//Then
		assertEquals(content, result);
	}
	
	/*
	 * Content findByIdWithLinks(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Content 찾기, Linke 페치조인 -> 존재하지 않음")
	void findByIdWithLinksTestNonExist() {
		//Given
		Long nonExistId = Long.valueOf(24);
		
		given(contentRepository.findByIdWithLinks(nonExistId)).willReturn(Optional.empty());
		
		//When
		Content result = contentService.findByIdWithLinks(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 Content 찾기, Linke 페치조인 -> 정상")
	void findByIdWithLinksTest() {
		//Given
		Long contentId = Long.valueOf(24);
		
		given(contentRepository.findByIdWithLinks(contentId)).willReturn(Optional.of(content));
		
		//When
		Content result = contentService.findByIdWithLinks(contentId);
		
		//Then
		assertEquals(content, result);
	}
	
	/*
	 * List<Content> findAllBySpaceIdWithLinks(Long spaceId)
	 */
	
	@Test
	@DisplayName("Space ID로 Content들 찾기, Links 페치 조인 -> 정상")
	void findAllBySpaceIdWithLinksTest() {
		//Given
		Long spaceId = Long.valueOf(1);
		
		//When
		contentService.findAllBySpaceIdWithLinks(spaceId);
		
		//Then
		then(contentRepository).should(times(1)).findAllBySpaceIdWithLinks(spaceId);
	}
	
	/*
	 * Content save(Content content)
	 */
	
	@Test
	@DisplayName("Content 생성 및 수정 -> 정상")
	void saveTest() {
		//Given
		given(contentRepository.save(content)).willReturn(content);
		
		//When
		Content result = contentService.save(content);
		
		//Then
		assertEquals(content, result);
	}
	
	/*
	 *  void delete(Content content)
	 */
	
	@Test
	@DisplayName("Content 삭제 -> 정상, check contentRepo")
	void deleteTestCheckContentRepo() {
		//Given
		
		//When
		contentService.delete(content);
		
		//Then
		then(contentRepository).should(times(1)).delete(content);
	}
	
	@Test
	@DisplayName("Content 삭제 -> 정상, check linkService")
	void deleteTestCheckLinkService() {
		//Given
		Long contentId = Long.valueOf(82);
		content.setId(contentId);
		
		//When
		contentService.delete(content);
		
		//Then
		then(linkService).should(times(1)).deleteAllByContentId(contentId);
	}
	
	/*
	 * void deleteById(Long id)
	 */
	
	@Test
	@DisplayName("ID에 해당하는 Content 삭제 -> 존재하지 않았음")
	void deleteByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(contentRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		contentService.deleteById(nonExistId);
		
		//Then
		then(contentRepository).should(never()).delete(any(Content.class));
	}
	
	@Test
	@DisplayName("ID에 해당하는 Content 삭제 -> 정상")
	void deleteByIdTest() {
		//Given
		Long contentId = Long.valueOf(1);
		
		given(contentRepository.findById(contentId)).willReturn(Optional.of(content));
		
		//When
		contentService.deleteById(contentId);
		
		//Then
		then(contentRepository).should(times(1)).delete(content);
	}
	
	/*
	 * void deleteAllBySpaceId(Long spaceId)
	 */
	
	@Test
	@DisplayName("Space ID로 Content들 삭제 -> 정상, check contentRepo")
	void deleteAllBySpaceIdTestCheckContentRepo() {
		//Given
		Long spaceId = Long.valueOf(62);
		List<Content> contentsForSpace = new ArrayList<>();
		given(contentRepository.findAllBySpaceIdInQuery(spaceId)).willReturn(contentsForSpace);
		
		//When
		contentService.deleteAllBySpaceId(spaceId);
		
		//Then
		then(contentRepository).should(times(1)).deleteAllBySpaceIdInQuery(spaceId);
	}
	
	@Test
	@DisplayName("Space ID로 Content들 삭제 -> 정상, check LinkService")
	void deleteAllBySpaceIdTestCheckLinkService() {
		//Given
		Long spaceId = Long.valueOf(79);
		int numOfContentsForSpace = 17;
		List<Content> contentsForSpace = getContentsForSpace(numOfContentsForSpace);
		given(contentRepository.findAllBySpaceIdInQuery(spaceId)).willReturn(contentsForSpace);
		
		//When
		contentService.deleteAllBySpaceId(spaceId);
		
		//Then
		then(linkService).should(times(numOfContentsForSpace)).deleteAllByContentId(anyLong());
	}
	
	private List<Content> getContentsForSpace(int numOfContentsForSpace) {
		List<Content> contents = new ArrayList<>();
		for(int i = 0; i < numOfContentsForSpace; i++) {
			Content content = ContentTest.getContentInstance();
			content.setId(Long.valueOf(1));
			contents.add(content);
		}
		return contents;
	}
	
	/*
	 * long countAll()
	 */
	
	@Test
	@DisplayName("모든 Content 개수 세기 -> 정상")
	void countAllTest() {
		//Given
		long num = 1;
		
		given(contentRepository.count()).willReturn(num);
		
		//When
		long result = contentService.countAll();
		
		//Then
		assertEquals(num, result);
	}
	
	/*
	 * boolean existsById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Content 존재 여부 반환 -> true")
	void existsByIdTrueTest() {
		//Given
		Long existId = Long.valueOf(1);
		boolean isExist = true;
		
		given(contentRepository.existsById(existId)).willReturn(isExist);
		
		//When
		boolean result = contentService.existsById(existId);
		
		//Then
		assertEquals(isExist, result);
	}
	
	@Test
	@DisplayName("ID로 Content 존재 여부 반환 -> false")
	void existsByIdFalseTest() {
		//Given
		Long nonExistId = Long.valueOf(1);
		boolean isExist = false;
		
		given(contentRepository.existsById(nonExistId)).willReturn(isExist);
		
		//When
		boolean result = contentService.existsById(nonExistId);
		
		//Then
		assertEquals(isExist, result);
	}
}
