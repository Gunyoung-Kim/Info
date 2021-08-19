package com.gunyoung.info.util;

import com.gunyoung.info.domain.Link;

/**
 * Test 클래스 전용 Link 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class LinkTest {
	
	/**
	 * 임의의 Link 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static Link getLinkInstance() {
		Link link = Link.builder()
				.tag("tag")
				.url("http://localhost")
				.build();
		
		return link;
	}
}
