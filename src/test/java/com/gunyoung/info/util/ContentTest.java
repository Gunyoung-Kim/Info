package com.gunyoung.info.util;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.dto.ContentDTO;

/**
 * Test 클래스 전용 Content 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class ContentTest {
	
	public static final String DEFAULT_CONTENT_TITLE = "title";
	
	/**
	 * 임의의 Content 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static Content getContentInstance() {
		return getContentInstance(DEFAULT_CONTENT_TITLE);
	}
	
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
	 * 테스트 용 {@link ContentDTO} 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static ContentDTO getContentDTOInstance() {
		ContentDTO contentDTO = ContentDTO.builder()
				.title(DEFAULT_CONTENT_TITLE)
				.description("description")
				.contributors("contributors")
				.skillstacks("skillstacks")
				.contents("contents")
				.build();
		return contentDTO;
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
