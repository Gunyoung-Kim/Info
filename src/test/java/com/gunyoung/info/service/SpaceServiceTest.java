package com.gunyoung.info.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Space;
import com.gunyoung.info.repos.SpaceRepository;
import com.gunyoung.info.services.domain.SpaceService;
import com.gunyoung.info.testutil.Integration;
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
	 *  - 대상 메소드:
	 *  	 public Space save(Space space);
	 */
	
	@Test
	@Transactional
	@DisplayName("Space save (성공, 수정)")
	public void saveSpaceTest() {
		//Given
		String changeDescription = "changeDescription";
		space.setDescription(changeDescription);
		
		Long spaceId = space.getId();
		
		//When
		spaceService.save(space);
		
		//Then
		assertEquals(changeDescription, spaceRepository.getById(spaceId).getDescription());
	}
	
}
