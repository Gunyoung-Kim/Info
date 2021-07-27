package com.gunyoung.info.controller;

import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.info.domain.Content;
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
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.services.domain.SpaceService;

import lombok.RequiredArgsConstructor;

/**
 * Content 도메인 관련 Request들을 처리하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class ContentController {
	
	private final PersonService personService;
	
	private final SpaceService spaceService;
	
	private final ContentService contentService;
	
	public static final int MAX_CONTENT_NUM = 50;
	
	/**
	 * <pre>
	 *  - 기능: url에 담긴 이메일 유저의 포트폴리오에 프로젝트 추가하는 페이지 반환하는 컨트롤러  
	 *  - 반환: 
	 *  	- 성공 
	 *  	View: createContent.html(포트폴리오에 프로젝트 추가하는 템플릿) 
	 *  	Model: formModel->Content(프로젝트 내용 추가할 Content 객체)
	 *  </pre>
	 *  @param email 콘텐트 추가하려는 사람의 이메일 주소
	 *  @throws NotMyResourceException 로그인된 계정과 일치하지 않으면
	 *  @throws PersonNotFoundedException 해당 이메일의 유저가 없으면
	 *  @throws ContentNumLimitExceedException 개인에게 할당 된 최대 프로젝트 수 초과 시 
	 *  @author kimgun-yeong
	 *  
	 */
	
	@RequestMapping(value="/space/makecontent/{email}", method = RequestMethod.GET)
	public ModelAndView createContent(@PathVariable String email,@ModelAttribute("formModel") Content content, ModelAndView mav) {
		// 해당 스페이스가 현재 접속자의 것인지 확인하는 작업
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(!email.equals(auth.getName())) {
			throw new NotMyResourceException(PersonErrorCode.RESOURCE_IS_NOT_MINE_ERROR.getDescription());
		}
		
		if(!personService.existsByEmail(email)) {
			throw new PersonNotFoundedException(PersonErrorCode.PERSON_NOT_FOUNDED_ERROR.getDescription());
		};
		
		Space space = personService.findByEmail(email).getSpace();
		
		if(space.getContents().size() >= MAX_CONTENT_NUM) {
			throw new ContentNumLimitExceedException(ContentErrorCode.CONTENT_NUM_LIMIT_EXCEEDED_ERROR.getDescription());
		}
		
		mav.addObject("formModel", content);
		
		mav.setViewName("createContent");
		
		return mav;
	}
	
	/**
	 * <pre>
	 *  - 기능: createContent가 반환한 뷰에 사용자가 프로젝트 정보들을 입력하고 POST 전송하여 이를 해당 사용자의 프로젝트로 추가하기 위한 컨트롤러
	 *  - 반환: 
	 *  	- 성공
	 *  	View: 메인 홈으로 redirect
	 *  	DB: Content 테이블에 로우 추가
	 *   </pre>
	 *   @param email 콘텐트 추가하려는 사람의 이메일 주소 
	 *   @param content 추가 되는 콘텐트 객체 
	 *   @throws NotMyResourceException 로그인된 정보가 해당 포트폴리오 주인이 아닐때 
	 *   @throws PersonNotFoundedException 일치하지만 데이터베이스에 저장되지 않은 이메일이면
	 *   @throws ContentNumLimitExceedException 개인 최대 프로젝트 개수 초과
	 *   @author kimgun-yeong
	 */
	@RequestMapping(value="/space/makecontent/{email}", method = RequestMethod.POST)
	public ModelAndView createContentPost(@PathVariable String email,@Valid @ModelAttribute("formModel") Content content ,ModelAndView mav) {
		
		// 해당 스페이스가 현재 접속자의 것인지 확인하는 작업
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(!email.equals(auth.getName())) {
			throw new NotMyResourceException(PersonErrorCode.RESOURCE_IS_NOT_MINE_ERROR.getDescription());
		}
		
		Person person = personService.findByEmail(email);
		if(person == null) {
			throw new PersonNotFoundedException(PersonErrorCode.PERSON_NOT_FOUNDED_ERROR.getDescription());
		}
		
		Space space = person.getSpace();
		
		if(space.getContents().size() >= MAX_CONTENT_NUM) {
			throw new ContentNumLimitExceedException(ContentErrorCode.CONTENT_NUM_LIMIT_EXCEEDED_ERROR.getDescription());
		}
		
		spaceService.addContent(space, content);
		
		return new ModelAndView("redirect:/space");
	}
	
	/**
	 * <pre>
	 *  - 기능: url에 입력된 id에 해당하는 콘텐츠의 정보 수정을 가능케하는 페이지 반환
	 *  - 반환: 
	 *  	- 성공
	 *		View: updateContent.html(콘텐츠의 정보를 수정하는 템플릿)
	 *		Model: formModel->ContentDTO(프로젝트 작성자 이메일 + 프로젝트 정보 DTO
	 * </pre>
	 * @param id 수정하려는 콘텐트의 id 값
	 * @throws ContentNotFoundedException 입력된 id에 해당하는 content가 DB 테이블에 없을때 
	 * @throws NotMyResourceException 현재 로그인 유저 != 해당 프로젝트 작성자
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/space/updatecontent/{id}", method= RequestMethod.GET)
	public ModelAndView updateContent(@PathVariable long id, @ModelAttribute("formModel") ContentDTO contentDto, ModelAndView mav) {
		if(!contentService.existsById(id)) {
			throw new ContentNotFoundedException(ContentErrorCode.CONTENT_NOT_FOUNDED_ERROR.getDescription());
		}
		Content content = contentService.findById(id);
		
		// 해당 컨텐트가 현재 접속자의 것인지 확인하는 작업
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String contentUserEmail = content.getSpace().getPerson().getEmail();
		
		if(!contentUserEmail.equals(auth.getName())) {
			throw new NotMyResourceException(PersonErrorCode.RESOURCE_IS_NOT_MINE_ERROR.getDescription()); 
		}
		
		contentDto.settingByEmailAndContent(contentUserEmail, content);
		mav.addObject("formModel", contentDto);
		
		mav.setViewName("updateContent");
		
		return mav;
	}
}
