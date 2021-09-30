package com.gunyoung.info.services.domain;

import java.util.List;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Space;

public interface SpaceService {
	
	/**
	 * ID로 Space 찾기 
	 * @param id 찾으려는 Space의 ID
	 * @return Space, null(id에 해당하는 Space 없을 때)
	 * @author kimgun-yeong
	 */
	public Space findById(Long id);
	
	/**
	 * 모든 Space 반환
	 * @author kimgun-yeong
	 */
	public List<Space> findAll();
	
	/**
	 * Space 생성 및 수정
	 * @param space 저장하려는 Space
	 * @return 저장된 Space
	 * @author kimgun-yeong
	 */
	public Space save(Space space);
	
	/**
	 * Space 삭제
	 * @param space 삭제하려는 Space, not null
	 * @author kimgun-yeong
	 */
	public void delete(Space space);
	
	/**
	 * Id로 Space 존재 여부 반환
	 * @param id 존재 여부 확인하려는 Space의 id
	 * @author kimgun-yeong
	 */
	public boolean existsById(Long id);
	
	/**
	 * Space의 Contents에 Content 추가 
	 * @param space Content 추가하려는 Space
	 * @param content Space에 추가하려는 Content
	 * @author kimgun-yeong
	 */
	public void addContent(Space space, Content content);
}
