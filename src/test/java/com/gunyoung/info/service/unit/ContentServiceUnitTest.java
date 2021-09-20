package com.gunyoung.info.service.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

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

/**
 * {@link ContentServiceImpl}에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) Service class only <br>
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class ContentServiceUnitTest {
	
	@Mock
	ContentRepository contentRepository;
	
	@InjectMocks
	ContentServiceImpl contentService;
	
	private Content content; 
	
	@BeforeEach
	void setup() {
		content = new Content();
	}
	
	/*
	 * public Content findById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Content 찾기 -> 존재하지 않음")
	public void findByIdNonExist() {
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
	public void findByIdTest() {
		//Given
		Long contentId = Long.valueOf(1);
		
		given(contentRepository.findById(contentId)).willReturn(Optional.of(content));
		
		//When
		Content result = contentService.findById(contentId);
		
		//Then
		assertEquals(content, result);
	}
	
	/*
	 * public Content findByIdWithSpaceAndPerson(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Content 찾기, Space Person 페치조인  -> 존재하지 않음")
	public void findByIdWithSpaceAndPersonNonExist() {
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
	public void findByIdWithSpaceAndPersonTest() {
		//Given
		Long contentId = Long.valueOf(1);
		
		given(contentRepository.findByIdWithSpaceAndPerson(contentId)).willReturn(Optional.of(content));
		
		//When
		Content result = contentService.findByIdWithSpaceAndPerson(contentId);
		
		//Then
		assertEquals(content, result);
	}
	
	/*
	 * public Content findByIdWithLinks(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Content 찾기, Linke 페치조인 -> 존재하지 않음")
	public void findByIdWithLinksTestNonExist() {
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
	public void findByIdWithLinksTest() {
		//Given
		Long contentId = Long.valueOf(24);
		
		given(contentRepository.findByIdWithLinks(contentId)).willReturn(Optional.of(content));
		
		//When
		Content result = contentService.findByIdWithLinks(contentId);
		
		//Then
		assertEquals(content, result);
	}
	
	/*
	 * public List<Content> findAllBySpaceIdWithLinks(Long spaceId)
	 */
	
	@Test
	@DisplayName("Space ID로 Content들 찾기, Links 페치 조인 -> 정상")
	public void findAllBySpaceIdWithLinksTest() {
		//Given
		Long spaceId = Long.valueOf(1);
		
		//When
		contentService.findAllBySpaceIdWithLinks(spaceId);
		
		//Then
		then(contentRepository).should(times(1)).findAllBySpaceIdWithLinks(spaceId);
	}
	
	/*
	 * public Content save(Content content)
	 */
	
	@Test
	@DisplayName("Content 생성 및 수정 -> 정상")
	public void saveTest() {
		//Given
		given(contentRepository.save(content)).willReturn(content);
		
		//When
		Content result = contentService.save(content);
		
		//Then
		assertEquals(content, result);
	}
	
	/*
	 *  public void delete(Content content)
	 */
	
	@Test
	@DisplayName("Content 삭제 -> 정상")
	public void deleteTest() {
		//Given
		
		//When
		contentService.delete(content);
		
		//Then
		then(contentRepository).should(times(1)).delete(content);
	}
	
	/*
	 * public void deleteById(Long id)
	 */
	
	@Test
	@DisplayName("ID에 해당하는 Content 삭제 -> 존재하지 않았음")
	public void deleteByIdNonExist() {
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
	public void deleteByIdTest() {
		//Given
		Long contentId = Long.valueOf(1);
		
		given(contentRepository.findById(contentId)).willReturn(Optional.of(content));
		
		//When
		contentService.deleteById(contentId);
		
		//Then
		then(contentRepository).should(times(1)).delete(content);
	}
	
	/*
	 * public long countAll()
	 */
	
	@Test
	@DisplayName("모든 Content 개수 세기 -> 정상")
	public void countAllTest() {
		//Given
		long num = 1;
		
		given(contentRepository.count()).willReturn(num);
		
		//When
		long result = contentService.countAll();
		
		//Then
		assertEquals(num, result);
	}
	
	/*
	 * public boolean existsById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Content 존재 여부 반환 -> true")
	public void existsByIdTrueTest() {
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
	public void existsByIdFalseTest() {
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
