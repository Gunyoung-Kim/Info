package com.gunyoung.info.services.domain;

import com.gunyoung.info.domain.Content;

public interface ContentService {
	
	/**
	 * ID로 Content 찾기 
	 * @param id 찾으려는 Content의 id
	 * @return Content, null ( 해당 id의 Content 없을 때)
	 * @author kimgun-yeong
	 */
	public Content findById(Long id);
	
	/**
	 * Content 생성 및 수정
	 * @param content 저장하려는 Content
	 * @return 저장된 Content
	 * @author kimgun-yeong
	 */
	public Content save(Content content);
	
	/**
	 * Content 삭제 
	 * @param content 삭제하려는 Content
	 * @author kimgun-yeong
	 */
	public void delete(Content content);
	
	/**
	 * Id에 해당하는 Content 삭제
	 * @param id 삭제하려는 Content의 Id
	 * @author kimgun-yeong
	 */
	public void deleteById(Long id);
	
	/**
	 * 모든 Content 개수 반환 
	 * @author kimgun-yeong
	 */
	public long countAll();
	
	/**
	 * ID에 해당하는 Content 존재 여부 반환
	 * @param id 존재여부 확인하려는 Content의 Id
	 * @author kimgun-yeong
	 */
	public boolean existsById(Long id);
}
