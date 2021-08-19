package com.gunyoung.info.util;

import com.gunyoung.info.domain.Space;

/**
 * Test 클래스 전용 Space 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class SpaceTest {
	
	/**
	 * 임의의 Space 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static Space getSpaceInstance() {
		Space space = Space.builder()
				.description("description")
				.build();
		
		return space;
	}
}
