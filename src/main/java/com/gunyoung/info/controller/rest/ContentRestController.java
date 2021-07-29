package com.gunyoung.info.controller.rest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.dto.ContentDTO;
import com.gunyoung.info.error.code.ContentErrorCode;
import com.gunyoung.info.error.code.PersonErrorCode;
import com.gunyoung.info.error.exceptions.access.NotMyResourceException;
import com.gunyoung.info.error.exceptions.nonexist.ContentNotFoundedException;
import com.gunyoung.info.error.exceptions.nonexist.PersonNotFoundedException;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.services.domain.PersonService;

import lombok.RequiredArgsConstructor;

/**
 * Content 관련 처리를 하는 RestController
 * @author kimgun-yeong
 *
 */
@RestController
@RequiredArgsConstructor
public class ContentRestController {
	
	private final ContentService contentService;
	
	private final PersonService personService;
	
	/**
	 * <pre>
	 *  - 기능: url에 명시된 id의 콘텐트 삭제하는 컨트롤러
	 *  - 반환:
	 *  	- 성공
	 *  	View: /space(현재 접속자의 포트폴리오) 로 redirect
	 *  	DB: content 삭제
	 *	</pre>
	 *	@param id 삭제하려는 콘텐트의 id 값
	 *  @throws ContentNotFoundedException 입력된 id에 해당하는 content가 DB 테이블에 없을때
	 *  @throws NotMyResourceException 현재 로그인 유저 != 해당 프로젝트 작성자
	 *	@author kimgun-yeong
	 */
	@RequestMapping(value="/space/deletecontent/{id}", method = RequestMethod.DELETE)
	public void deleteContent(@PathVariable long id) {
		if(!contentService.existsById(id)) {
			throw new ContentNotFoundedException(ContentErrorCode.CONTENT_NOT_FOUNDED_ERROR.getDescription());
		}
		Content targetContent = contentService.findById(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String hostEmail = targetContent.getSpace().getPerson().getEmail();
		if(!hostEmail.equals(auth.getName())) {
			// 리퀘스트 보낸 사람이 이 콘텐츠의 주인과 다를때 
			throw new NotMyResourceException(PersonErrorCode.RESOURCE_IS_NOT_MINE_ERROR.getDescription());
		}
		
		contentService.deleteContent(targetContent);
	}
	
	/**
	 * <pre>
	 *  - 기능: updateContent가 반환한 뷰에서 수정한 Content 정보를 반영하기 위한 사용자의 POST Request를 담당하는 컨트롤러
	 *  - 반환:
	 *  	- 성공
	 * 		View: /space(현재 접속자의 포트폴리오) 로 redirect
	 * 		DB: content 변경 사항 save
	 *	</pre>
	 * @param id 수정하려는 콘텐트의 id 값
	 * @param contentDto 콘텐트에 대한 수정 사항을 담은 DTO 객체
	 * @throws ContentNotFoundedException 입력된 id에 해당하는 content가 DB 테이블에 없을때
	 * @throws PersonNotFoundedException 폼에 보내진 hostId에 해당하는 Person 없을때
	 * @throws NotMyResourceException 현재 로그인 유저 != 해당 프로젝트 작성자
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/space/updatecontent/{id}", method= RequestMethod.PUT) 
	public void updateContentPost(@PathVariable long id, @ModelAttribute ContentDTO contentDto) {
		if(!contentService.existsById(id)) {
			throw new ContentNotFoundedException(ContentErrorCode.CONTENT_NOT_FOUNDED_ERROR.getDescription());
		}
		Content content = contentService.findById(id);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Long hostId = contentDto.getHostId();
		
		Person host = personService.findById(hostId);
		
		if(host == null) {
			throw new PersonNotFoundedException(PersonErrorCode.PERSON_NOT_FOUNDED_ERROR.getDescription());
		}
		
		String hostEmail = host.getEmail();
		
		if(!hostEmail.equals(auth.getName())) {
			// 리퀘스트 보낸 사람이 이 콘텐츠의 주인과 다를때 
			throw new NotMyResourceException(PersonErrorCode.RESOURCE_IS_NOT_MINE_ERROR.getDescription());
		}
		
		content.setTitle(contentDto.getTitle());
		content.setDescription(contentDto.getDescription());
		content.setContributors(contentDto.getContributors());
		content.setSkillstacks(contentDto.getSkillstacks());
		content.setStartedAt(contentDto.getStartedAt());
		content.setEndAt(contentDto.getEndAt());
		content.setContents(contentDto.getContents());
		content.setLinks(contentDto.getLinks());
		
		contentService.save(content);
	}
}
