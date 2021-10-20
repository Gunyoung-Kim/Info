package com.gunyoung.info.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Link;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.dto.ContentDTO;
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
 * Content 도메인 관련 화면 반환 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class ContentController {
	
	private final PersonService personService;
	
	private final ContentService contentService;
	
	private final LinkService linkService;
	
	/**
	 * 세션 유저의 포트폴리오에 프로젝트 추가화면으로 리다이렉트
	 * @author kimgun-yeong
	 */
	@GetMapping(value="/space/makecontent")
	public ModelAndView createMyContentView() {
		String loginUserEmail = AuthorityUtil.getSessionUserEmail();
		Person loginUser = personService.findByEmailWithSpace(loginUserEmail);
		if(loginUser == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Long loginUserId = loginUser.getId();
		return new ModelAndView("redirect:/space/makecontent/" + loginUserId);
	}
	
	/**
	 * <pre>
	 *  - 기능: 유저의 포트폴리오에 프로젝트 추가하는 페이지 반환
	 *  - View: createContent.html(포트폴리오에 프로젝트 추가하는 템플릿) 
	 *  - Model: formModel -> Content(프로젝트 내용 추가할 Content 객체)
	 *  </pre>
	 *  @param personId 콘텐트 추가하려는 Person의 Id
	 *  @throws NotMyResourceException 로그인된 계정과 일치하지 않으면
	 *  @throws PersonNotFoundedException 해당 이메일의 유저가 없으면
	 *  @throws ContentNumLimitExceedException 개인에게 할당 된 최대 프로젝트 수 초과 시 
	 *  @author kimgun-yeong
	 *  
	 */
	@GetMapping(value="/space/makecontent/{personId}")
	public ModelAndView createContentView(@PathVariable Long personId,@ModelAttribute("formModel") Content content, ModelAndView mav) {
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
		
		mav.addObject("formModel", content);
		mav.addObject("personId", personId);
		
		mav.setViewName("createContent");
		
		return mav;
	}
	
	/**
	 * <pre>
	 *  - 기능: url에 입력된 id에 해당하는 콘텐츠의 정보 수정을 가능케하는 페이지 반환
	 *  - View: updateContent.html(콘텐츠의 정보를 수정하는 템플릿)
	 *  - Model: formModel -> ContentDTO(프로젝트 작성자 이메일 + 프로젝트 정보 DTO)
	 * </pre>
	 * @param contentId 수정하려는 콘텐트의 id 값
	 * @throws ContentNotFoundedException 입력된 id에 해당하는 content가 DB 테이블에 없을때 
	 * @throws NotMyResourceException 현재 로그인 유저 != 해당 프로젝트 작성자
	 * @author kimgun-yeong
	 */
	@GetMapping(value="/space/updatecontent/{contentId}")
	public ModelAndView updateContentView(@PathVariable Long contentId, @ModelAttribute("formModel") ContentDTO contentDto, ModelAndView mav) {
		Content content = contentService.findByIdWithSpaceAndPerson(contentId);
		if(content == null) {
			throw new ContentNotFoundedException(ContentErrorCode.CONTENT_NOT_FOUNDED_ERROR.getDescription());
		}
		
		Person contentHost = content.getSpace().getPerson();
		if(isContentHostAndSessionPersonNotMatch(contentHost)) {
			throw new NotMyResourceException(PersonErrorCode.RESOURCE_IS_NOT_MINE_ERROR.getDescription()); 
		}
		
		Long contentHostId = contentHost.getId();
		contentDto.settingHostIdAndContentField(contentHostId, content);
		
		List<Link> linkList = linkService.findAllByContentId(contentId);
		contentDto.settingLinks(linkList);
		
		mav.addObject("formModel", contentDto);
		mav.addObject("contentId", contentId);
		
		mav.setViewName("updateContent");
		
		return mav;
	}
	
	private boolean isContentHostAndSessionPersonNotMatch(Person contentHost) {
		String loginUserEmail = AuthorityUtil.getSessionUserEmail();
		String contentHostEmail = contentHost.getEmail();
		return !contentHostEmail.equals(loginUserEmail);
	}
}
