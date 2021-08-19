package com.gunyoung.info.util;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.info.domain.Content;

/**
 * Test 클래스 전용 Content 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class ContentTest {
	/**
	 * 임의의 Content 인스턴스 반환
	 * @param title 생성하려는 Content title
	 * @author kimgun-yeong
	 */
	public static Content getContentInstance(String title) {
		Content content = Content.builder()
				.title(title)
				.description("description")
				.contributors("contributors")
				.contents("contents")
				.build();
		
		return content;
	}
	
	/**
	 * Repository에 저장되지 않은 Content의 ID 반환
	 * @author kimgun-yeong
	 */
	public static Long getNonExistContentId(JpaRepository<Content, Long> repository) {
		Long nonExistContentId = Long.valueOf(1);
		
		for(Content p : repository.findAll()) {
			nonExistContentId = Math.max(nonExistContentId, p.getId());
		}
		
		nonExistContentId++;
		
		return nonExistContentId;
	}
}
