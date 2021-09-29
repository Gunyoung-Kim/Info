package com.gunyoung.info.services.domain;

import java.util.List;

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
	 * ID로 Content 찾기 <br>
	 * Space, Person 페치 조인
	 * @param contentId 찾으려는 Content의 ID
	 * @author kimgun-yeong
	 */
	public Content findByIdWithSpaceAndPerson(Long id);
	
	/**
	 * ID로 Content 찾기 <br>
	 * Links 페치 조인
	 * @param contentId 찾으려는 Content의 ID
	 * @author kimgun-yeong
	 */
	public Content findByIdWithLinks(Long id);
	
	/**
	 * Space ID로 Content들 찾기 <br>
	 * Links 페치 조인
	 * @param spaceId 찾으려는 Content들의 Space ID
	 * @author kimgun-yeong
	 */
	public List<Content> findAllBySpaceIdWithLinks(Long spaceId);
	
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
	 * Space ID로 해당 Content들 모두 삭제 
	 * @param spaceId 삭제하려는 Content들의 Space ID
	 * @author kimgun-yeong
	 */ 
	public void deleteAllBySpaceId(Long spaceId);
	
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
