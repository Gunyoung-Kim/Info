package com.gunyoung.info.service.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.repos.SpaceRepository;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.services.domain.SpaceServiceImpl;

/**
 * {@link SpaceServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) SpaceServiceImpl only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class SpaceServiceUnitTest {
	
	@Mock
	SpaceRepository spaceRepository;
	
	@Mock
	ContentService contentService;
	
	@InjectMocks 
	SpaceServiceImpl spaceService;
	
	private Space space;
	
	@BeforeEach
	void setup() {
		space = new Space();
		space.setContents(new ArrayList<>());
	}
	
	/*
	 * public Space findById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Space 찾기 -> 존재하지 않음")
	public void findByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(spaceRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		Space result = spaceService.findById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 Space 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long spaceId = Long.valueOf(1);
		
		given(spaceRepository.findById(spaceId)).willReturn(Optional.of(space));
		
		//When
		Space result = spaceService.findById(spaceId);
		
		//Then
		assertEquals(space, result);
	}
	
	/*
	 * public List<Space> findAll()
	 */
	
	@Test
	@DisplayName("모든 Space 찾기 -> 정상")
	public void findAllTest() {
		//Given
		List<Space> spaces = new ArrayList<>();
		
		given(spaceRepository.findAll()).willReturn(spaces);
		
		//When
		List<Space> result = spaceService.findAll();
		
		//Then
		assertEquals(spaces, result);
	}
	
	/*
	 * public Space save(Space space)
	 */
	
	@Test
	@DisplayName("Space 생성 및 수정 -> 정상")
	public void saveTest() {
		//Given
		given(spaceRepository.save(space)).willReturn(space);
		
		//When
		Space result = spaceService.save(space);
		
		//Then
		assertEquals(space, result);
	}
	
	/*
	 * public void delete(Space space) 
	 */
	
	@Test
	@DisplayName("Person ID로 Space 삭제 -> 정상")
	public void deleteByPersonIdTest() {
		//Given
		Long spaceId = Long.valueOf(25);
		space.setId(spaceId);
		
		//When
		spaceService.delete(space);
		
		//Then
		then(contentService).should(times(1)).deleteAllBySpaceId(spaceId);
		then(spaceRepository).should(times(1)).deleteByIdInQuery(spaceId);
	}
	
	/*
	 * public boolean existsById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Space 존재 여부 반환 -> true")
	public void existsByIdTrueTest() {
		//Given
		Long existId = Long.valueOf(1);
		boolean isExist = true;
		
		given(spaceRepository.existsById(existId)).willReturn(isExist);
		
		//When
		boolean result = spaceService.existsById(existId);
		
		//Then
		assertEquals(isExist, result);
	}
	
	@Test
	@DisplayName("ID로 Space 존재 여부 반환 -> false")
	public void existsByIdFalseTest() {
		//Given
		Long nonExistId = Long.valueOf(1);
		boolean isExist = false;
		
		given(spaceRepository.existsById(nonExistId)).willReturn(isExist);
		
		//When
		boolean result = spaceService.existsById(nonExistId);
		
		//Then
		assertEquals(isExist, result);
	}
	
	/*
	 * public void addContent(Space space, Content content);
	 */
	
	@Test
	@DisplayName("Space의 Contents에 Content 추가 -> 정상")
	public void addContentTest() {
		//Given
		Content content = new Content();
		
		//When
		spaceService.addContent(space, content);
		
		//Then
		then(contentService).should(times(1)).save(content);
		
		assertTrue(space.getContents().contains(content));
	}
	
}

