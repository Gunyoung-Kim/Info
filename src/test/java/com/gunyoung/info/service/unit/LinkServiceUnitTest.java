package com.gunyoung.info.service.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
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
import com.gunyoung.info.domain.Link;
import com.gunyoung.info.dto.LinkDTO;
import com.gunyoung.info.repos.LinkRepository;
import com.gunyoung.info.services.domain.LinkServiceImpl;

/**
 * {@link LinkServiceImpl}에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) Service class only <br>
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class LinkServiceUnitTest {
	
	@Mock 
	LinkRepository linkRepository;
	
	@InjectMocks 
	LinkServiceImpl linkService;
	
	private Link link;
	
	@BeforeEach
	void setup() {
		link = Link.builder()
				.build();
	}
	
	/*
	 * public Link findById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Link 찾기 -> 존재하지 않음")
	public void findByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(linkRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		Link result = linkService.findById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 Link 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long linkId = Long.valueOf(1);
		
		given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
		
		//When
		Link result = linkService.findById(linkId);
		
		//Then
		assertEquals(link, result);
	}
	
	/*
	 *public List<Link> findAllByContentId(Long contentId)  
	 */
	
	@Test
	@DisplayName("Content ID로 Link들 찾기 -> 정상")
	public void findAllByContentIdTest() {
		//Given
		Long contentId = Long.valueOf(1);
		
		//When
		linkService.findAllByContentId(contentId);
		
		//Then
		then(linkRepository).should(times(1)).findAllByContentId(contentId);
	}
	
	/*
	 * public Link save(Link link)
	 */
	
	@Test
	@DisplayName("Link 생성 및 수정 -> 정상")
	public void saveTest() {
		//Given
		given(linkRepository.save(link)).willReturn(link);
		
		//When
		Link result = linkService.save(link);
		
		//Then
		assertEquals(link, result);
	}
	
	/*
	 * public List<Link> updateLinksForContent(Content content,Iterable<LinkDTO> linkDTOs, Iterable<Link> existContentLinks)
	 */
	
	@Test
	@DisplayName("기존의 Content의 Link들을 LinkDTO를 통해 업데이트 -> 기존의 Link 삭제")
	public void updateLinksForContentContentDelete() {
		//Given
		Content content = Content.builder().build();
		List<LinkDTO> linkDTOs = new ArrayList<>();
		
		Long existLinkId = Long.valueOf(1);
		Link existbutDeleteSoonLink = Link.builder()
				.id(existLinkId)
				.tag("tag")
				.url("test.com")
				.build();
		content.getLinks().add(existbutDeleteSoonLink);
		
		//When
		linkService.updateLinksForContent(content, linkDTOs);
		
		//Then
		then(linkRepository).should(times(1)).delete(any(Link.class));
	}
	
	@Test
	@DisplayName("기존의 Content의 Link들을 LinkDTO를 통해 업데이트 -> 기존의 Link 수정")
	public void updateLinksForContentContentUpdate() {
		//Given
		Content content = Content.builder().build();
		List<LinkDTO> linkDTOs = new ArrayList<>();
		
		Long existLinkId = Long.valueOf(1);
		Link existLink = Link.builder()
				.id(existLinkId)
				.tag("tag")
				.url("test.com")
				.build();
		content.getLinks().add(existLink);
		
		String changeTag = "changedTag";
		LinkDTO linkDTOToUpdate = LinkDTO.builder()
				.id(existLinkId)
				.tag(changeTag)
				.url(existLink.getUrl())
				.build();
		linkDTOs.add(linkDTOToUpdate);
		
		//When
		linkService.updateLinksForContent(content, linkDTOs);
		
		//Then
		assertEquals(changeTag, existLink.getTag());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("기존의 Content의 Link들을 LinkDTO를 통해 업데이트 -> 새로운 Link 추가")
	public void updateLinksForContentNewLinkTest() {
		//Given
		Content content = Content.builder().build();
		List<LinkDTO> linkDTOs = new ArrayList<>();
		
		LinkDTO linkDTO = LinkDTO.builder()
				.tag("tag")
				.url("test.com")
				.build();
		linkDTOs.add(linkDTO);
		
		//When
		linkService.updateLinksForContent(content, linkDTOs);
		
		//Then
		then(linkRepository).should(times(1)).saveAll(any(Iterable.class));
	}
	
	/*
	 *  public void delete(Link link)
	 */
	
	@Test
	@DisplayName("Link 삭제 -> 정상")
	public void deleteTest() {
		//Given
		
		//When
		linkService.delete(link);
		
		//Then
		then(linkRepository).should(times(1)).delete(link);
	}
	
	/*
	 * public void deleteById(Long id)
	 */
	
	@Test
	@DisplayName("ID에 해당하는 Link 삭제 -> 존재하지 않았음")
	public void deleteByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(linkRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		linkService.deleteById(nonExistId);
		
		//Then
		then(linkRepository).should(never()).delete(any(Link.class));
	}
	
	@Test
	@DisplayName("ID에 해당하는 Link 삭제 -> 정상")
	public void deleteByIdTest() {
		//Given
		Long linkId = Long.valueOf(1);
		
		given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
		
		//When
		linkService.deleteById(linkId);
		
		//Then
		then(linkRepository).should(times(1)).delete(link);
	}
	
	/*
	 * public void deleteAllByContentId(Long contentId)
	 */
	
	@Test
	@DisplayName("Content ID에 해당하는 Link들 삭제 -> 정상")
	public void deleteAllByContentIdTest() {
		//Given
		Long contentId = Long.valueOf(24);
		
		//When
		linkService.deleteAllByContentId(contentId);
		
		//Then
		then(linkRepository).should(times(1)).deleteAllByContentIdInQuery(contentId);
	}
}
