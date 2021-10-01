package com.gunyoung.info.util;

import org.springframework.data.jpa.repository.JpaRepository;

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
	
	/**
	 * repository에 저장되지 않은 Link ID 반환
	 * @author kimgun-yeong
	 */
	public static Long getNonExistLinkId(JpaRepository<Link, Long> repository) {
		Long nonExistLinkId = Long.valueOf(1);
		for(Link link : repository.findAll()) {
			nonExistLinkId = Math.max(nonExistLinkId, link.getId());
		}
		
		nonExistLinkId++;
		
		return nonExistLinkId;
	}
}
