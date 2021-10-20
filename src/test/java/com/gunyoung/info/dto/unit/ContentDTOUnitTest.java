package com.gunyoung.info.dto.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Link;
import com.gunyoung.info.dto.ContentDTO;
import com.gunyoung.info.dto.LinkDTO;
import com.gunyoung.info.util.ContentTest;
import com.gunyoung.info.util.LinkTest;

/**
 * {@link ContentDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ContentDTO only
 * @author kimgun-yeong
 *
 */
class ContentDTOUnitTest {
	
	private ContentDTO contentDTO;
	
	@BeforeEach
	void setup() {
		contentDTO = ContentTest.getContentDTOInstance();
	}
 	
	/*
	 * void settingHostIdAndContentField(Long hostId, Content content)
	 */
	
	@Test
	@DisplayName("Content Host Person Id, Content 필드들로 ContentDTO 필드 값 설정 -> 정상,필드 값 확인")
	void settingHostIdAndContentFieldTestCheckField() {
		//Given
		Content content = ContentTest.getContentInstance();
		
		Long hostId = Long.valueOf(24);
		
		//When
		contentDTO.settingHostIdAndContentField(hostId, content);
		
		//Then
		verifyConetenDTOWithContentAndHostId(contentDTO, hostId, content);
	}
	
	private void verifyConetenDTOWithContentAndHostId(ContentDTO contentDTO, Long hostId, Content content) {
		assertEquals(hostId, contentDTO.getHostId());
		assertEquals(content.getTitle(), contentDTO.getTitle());
		assertEquals(content.getDescription(), contentDTO.getDescription());
		assertEquals(content.getSkillstacks(), contentDTO.getSkillstacks());
		assertEquals(content.getStartedAt(), contentDTO.getStartedAt());
		assertEquals(content.getEndAt(), contentDTO.getEndAt());
		assertEquals(content.getContents(), contentDTO.getContents());
	}
	
	/*
	 * void settingLinks(List<Link> links)
	 */
	
	@Test
	@DisplayName("Link 리스트를 통해 links 필드 설정 -> 정상")
	void settingLinksTest() {
		//Given
		Long givenLinkNum = Long.valueOf(14);
		List<Link> givenLinks = new ArrayList<>();
		
		for(int i=0;i < givenLinkNum; i++) {
			Link link = LinkTest.getLinkInstance();
			givenLinks.add(link);
		}
		
		contentDTO.setLinks(new ArrayList<>());
		
		//When
		contentDTO.settingLinks(givenLinks);
		
		//Then
		assertEquals(givenLinkNum, contentDTO.getLinks().size());
	}
	
	/*
	 * void updateContentOnly(Content content)
	 */
	
	@Test
	@DisplayName("ContentDTO에 담긴 정보로 Content만 업데이트 -> 정상, 필드 값 확인")
	void updateContentOnlyTestCheckField() {
		//Given
		Content content = Content.builder().build();
		
		//When
		contentDTO.updateContentOnly(content);
		
		//Then
		verifyContentWithContentDTO(contentDTO, content);
	}
	
	/*
	 * Content createContentOnly()
	 */
	
	@Test
	@DisplayName("ContentDTO에 담긴 정보로 Content 생성 후 반환-> 정상, 필드 값 확인")
	void createContentOnlyTestCheckField() {
		//Given
		
		//When
		Content result = contentDTO.createContentOnly();
		
		//Then
		verifyContentWithContentDTO(contentDTO, result);
	}
	
	private void verifyContentWithContentDTO(ContentDTO contentDTO, Content content) {
		assertEquals(contentDTO.getTitle(), content.getTitle());
		assertEquals(contentDTO.getDescription(), content.getDescription());
		assertEquals(contentDTO.getContributors(), content.getContributors());
		assertEquals(contentDTO.getSkillstacks(), content.getSkillstacks());
		assertEquals(contentDTO.getStartedAt(), content.getStartedAt());
		assertEquals(contentDTO.getEndAt(), content.getEndAt());
		assertEquals(contentDTO.getContents(), content.getContents());
	}
	
	
	/*
	 * List<Link> createLinkListOnlyWithContent(Content content)
	 */
	
	@Test
	@DisplayName("ContentDTO에 담긴 정보로 Link 리스트 생성 후 반환 -> 정상")
	void createLinkListOnlyWithContentTest() {
		//Given
		Content content = ContentTest.getContentInstance();
		
		Long givenLinkDTONum = Long.valueOf(14);
		List<LinkDTO> givenLinkDTOs = new ArrayList<>();
		
		for(int i=0;i < givenLinkDTONum; i++) {
			LinkDTO linkDTO = LinkDTO.builder()
					.tag("tag")
					.url("127.0.0.1")
					.build();
			givenLinkDTOs.add(linkDTO);
		}
		
		contentDTO.setLinks(givenLinkDTOs);
		
		//When
		List<Link> result = contentDTO.createLinkListOnlyWithContent(content);
		
		//Then
		assertEquals(givenLinkDTONum, result.size());
	}
}
