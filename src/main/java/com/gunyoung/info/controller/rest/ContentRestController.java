package com.gunyoung.info.controller.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Link;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.dto.ContentDTO;
import com.gunyoung.info.dto.LinkDTO;
import com.gunyoung.info.error.code.ContentErrorCode;
import com.gunyoung.info.error.code.PersonErrorCode;
import com.gunyoung.info.error.exceptions.access.NotMyResourceException;
import com.gunyoung.info.error.exceptions.exceed.ContentNumLimitExceedException;
import com.gunyoung.info.error.exceptions.nonexist.ContentNotFoundedException;
import com.gunyoung.info.error.exceptions.nonexist.PersonNotFoundedException;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.services.domain.LinkService;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.util.AuthorityUtil;

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
	
	private final LinkService linkService;
	
	/**
	 * <pre>
	 *  - 기능: createContent가 반환한 뷰에 사용자가 프로젝트 정보들을 입력하고 POST 전송하여 이를 해당 사용자의 프로젝트로 추가하기 위한 컨트롤러
	 *  - 반환: 
	 *  	- 성공
	 *  	DB: Content 테이블에 로우 추가
	 *   </pre>
	 *   @param personId 콘텐트 추가하려는 Person ID
	 *   @param content 추가 되는 콘텐트 객체 
	 *   @throws NotMyResourceException 로그인된 정보가 해당 포트폴리오 주인이 아닐때 
	 *   @throws PersonNotFoundedException 일치하지만 데이터베이스에 저장되지 않은 이메일이면
	 *   @throws ContentNumLimitExceedException 개인 최대 프로젝트 개수 초과
	 *   @author kimgun-yeong
	 */
	@RequestMapping(value="/space/makecontent/{personId}", method = RequestMethod.POST)
	public void createContent(@PathVariable Long personId, @Valid @ModelAttribute ContentDTO contentDTO) {
		// 해당 스페이스가 현재 접속자의 것인지 확인하는 작업
		String loginUserEmail = AuthorityUtil.getSessionUserEmail();
		Person loginUser = personService.findByEmailWithSpace(loginUserEmail);
		if(loginUser == null) {
			throw new PersonNotFoundedException(PersonErrorCode.PERSON_NOT_FOUNDED_ERROR.getDescription());
		}
		
		Long loginUserId = loginUser.getId();
		if(!personId.equals(loginUserId)) {
			throw new NotMyResourceException(PersonErrorCode.RESOURCE_IS_NOT_MINE_ERROR.getDescription());
		}
		
		Space space = loginUser.getSpace();
		if(space.getContents().size() >= Space.MAX_CONTENT_NUM) {
			throw new ContentNumLimitExceedException(ContentErrorCode.CONTENT_NUM_LIMIT_EXCEEDED_ERROR.getDescription());
		}
		
		Content newContent = contentDTO.createContentOnly();
		newContent.setSpace(space);
		contentService.save(newContent);
		
		List<Link> newLinkList = contentDTO.createLinkListOnlyWithContent(newContent);
		linkService.saveAll(newLinkList);
	}
	
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
		Content targetContent = contentService.findByIdWithSpaceAndPerson(id);
		if(targetContent == null) {
			throw new ContentNotFoundedException(ContentErrorCode.CONTENT_NOT_FOUNDED_ERROR.getDescription());
		}
		
		if(isContentIsNotLoginUsersContent(targetContent)) {
			throw new NotMyResourceException(PersonErrorCode.RESOURCE_IS_NOT_MINE_ERROR.getDescription());
		}
		
		contentService.delete(targetContent);
	}
	
	private boolean isContentIsNotLoginUsersContent(Content targetContent) {
		String loginUserEmail = AuthorityUtil.getSessionUserEmail();
		String hostEmail = targetContent.getSpace().getPerson().getEmail();
		return !hostEmail.equals(loginUserEmail);
	}
	
	/**
	 * <pre>
	 *  - 기능: updateContent가 반환한 뷰에서 수정한 Content 정보를 반영하기 위한 사용자의 POST Request를 담당하는 컨트롤러
	 *  - 반환:
	 *  	- 성공
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
	public void updateContent(@PathVariable long id, @ModelAttribute ContentDTO contentDto) {
		Content targetContent = contentService.findByIdWithLinks(id);
		if(targetContent == null) {
			throw new ContentNotFoundedException(ContentErrorCode.CONTENT_NOT_FOUNDED_ERROR.getDescription());
		}
		
		Long contentHostId = contentDto.getHostId();
		Person host = personService.findById(contentHostId);
		if(host == null) {
			throw new PersonNotFoundedException(PersonErrorCode.PERSON_NOT_FOUNDED_ERROR.getDescription());
		}
		
		String hostEmail = host.getEmail();
		if(isHostEmailAndLoginUserEmailMisMatch(hostEmail)) { 
			throw new NotMyResourceException(PersonErrorCode.RESOURCE_IS_NOT_MINE_ERROR.getDescription());
		}
		
		contentDto.updateContentOnly(targetContent);
		contentService.save(targetContent);
		
		saveNewLinksForContentFromContentDTO(targetContent, contentDto);
	}
	
	private boolean isHostEmailAndLoginUserEmailMisMatch(String hostEmail) {
		String loginUserEmail = AuthorityUtil.getSessionUserEmail();
		return !hostEmail.equals(loginUserEmail);
	}
	
	private void saveNewLinksForContentFromContentDTO(Content targetContent, ContentDTO contentDTO) {
		List<LinkDTO> linkDTOs = contentDTO.getLinks();
		List<Link> existContentLinks = targetContent.getLinks();
		
		linkService.saveByLinkDTOsAndExistContentLinks(targetContent,linkDTOs,existContentLinks);
	}
}
