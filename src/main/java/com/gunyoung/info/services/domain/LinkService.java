package com.gunyoung.info.services.domain;

import java.util.List;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Link;
import com.gunyoung.info.dto.LinkDTO;

public interface LinkService {
	
	/**
	 * ID로 Link 찾기 
	 * @param id 찾으려는 Link의 Id
	 * @return Link, null(id의 Link 없을 때)
	 * @author kimgun-yeong
	 */
	public Link findById(Long id);
	
	/**
	 * Content Id를 만족하는 모든 Link 반환
	 * @param contentId 찾으려는 Link들의 Content ID
	 * @author kimgun-yeong
	 */
	public List<Link> findAllByContentId(Long contentId);
	
	/**
	 * Link 생성 및 수정
	 * @param link 저장하려는 Link
	 * @return 저장된 Link
	 * @author kimgun-yeong
	 */
	public Link save(Link link);
	
	/**
	 * 기존의 Content의 Link들을 LinkDTO를 통해 업데이트
	 * @author kimgun-yeong
	 */
	public List<Link> updateLinksForContent(Content content, Iterable<LinkDTO> linkDTOs);
	
	/**
	 * 모든 Link들 저장
	 * @author kimgun-yeong
	 */
	public List<Link> saveAll(Iterable<Link> links);
	
	/**
	 * Link 삭제
	 * @param link 삭제하려는 Link
	 * @author kimgun-yeong
	 */
	public void delete(Link link);
	
	/**
	 * ID에 해당하는 Link 삭제
	 * @param id 삭제하려는 Link의 ID
	 * @author kimgun-yeong
	 */
	public void deleteById(Long id);
	
	/**
	 * Content Id로 해당 Link들 모두 삭제 
	 * @param contentId 삭제하려는 Link들의 Content ID
	 * @author kimgun-yeong
	 */
	public void deleteAllByContentId(Long contentId);
}
