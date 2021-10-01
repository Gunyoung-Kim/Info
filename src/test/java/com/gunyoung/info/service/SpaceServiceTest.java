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
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.repos.ContentRepository;
import com.gunyoung.info.repos.PersonRepository;
import com.gunyoung.info.repos.SpaceRepository;
import com.gunyoung.info.services.domain.SpaceService;
import com.gunyoung.info.testutil.Integration;
import com.gunyoung.info.util.ContentTest;
import com.gunyoung.info.util.SpaceTest;

/**
 * {@link SpaceService} 에 대한 테스트 클래스 <br>
 * 테스트 범위:(통합 테스트) 서비스 계층 - 영속성 계층 
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
public class SpaceServiceTest {
	
	@Autowired
	SpaceRepository spaceRepository;
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	ContentRepository contentRepository;
	
	@Autowired
	SpaceService spaceService;
	
	private Space space;
	
	@BeforeEach
	void setup() {
		space = SpaceTest.getSpaceInstance();
		spaceRepository.save(space);
	}
	
	@AfterEach
	void tearDown() {
		spaceRepository.deleteAll();
	}
	
	/*
	 *   public Space save(Space space);
	 */
	
	@Test
	@DisplayName("Space save (성공, 수정)")
	public void saveSpaceTest() {
		//Given
		String changeDescription = "changeDescription";
		space.setDescription(changeDescription);
		
		Long spaceId = space.getId();
		
		//When
		spaceService.save(space);
		
		//Then
		assertEquals(changeDescription, spaceRepository.findById(spaceId).get().getDescription());
	}
	
	/*
	 * public void delete(Space space)
	 */
	
	@Test
	@Transactional
	@DisplayName(" Space 삭제 -> 정상, Space 삭제 확인")
	public void deleteTestCheckSpaceRemove() {
		//Given
		Long spaceId = space.getId();
		
		//When
		spaceService.delete(space);
		
		//Then
		assertFalse(spaceRepository.existsById(spaceId));
	}
	
	@Test
	@Transactional
	@DisplayName("Space 삭제 -> 정상, 관련 Content 삭제 확인")
	public void deleteTestCheckContentsRemove() {
		//Given
		int newContentsNum = 10;
		addNewContentsForSpace(newContentsNum);
		long beforeContentNum = contentRepository.count();
		
		//When
		spaceService.delete(space);
		
		//Then
		assertEquals(beforeContentNum - newContentsNum, contentRepository.count());
	}
	
	private List<Content> addNewContentsForSpace(int newContentsNum) {
		List<Content> newContents = new ArrayList<>();
		for(int i=0; i < newContentsNum; i++) {
			Content content = ContentTest.getContentInstance();
			content.setSpace(space);
			newContents.add(content);
		}
		contentRepository.saveAll(newContents);
		return newContents;
	}
}
