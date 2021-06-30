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
	 *      - 에러  
	 *  	로그인된 계정과 일치하지 않으면 메인 홈으로 redirect 
	 *  	일치하지만 데이터베이스에 저장되지 않은 이메일이면 실패 페이지 반환 -> 실제로 일어나지 않을 상황이지 않을까?
	 *  </pre>
	 *  @param email 콘텐트 추가하려는 사람의 이메일 주소
	 *  @author kimgun-yeong
	 *  
	 */
	
	@RequestMapping(value="/space/makecontent/{email}", method = RequestMethod.GET)
	public ModelAndView createContent(@PathVariable String email,@ModelAttribute("formModel") Content content, ModelAndView mav) {
		// 해당 스페이스가 현재 접속자의 것인지 확인하는 작업
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(!email.equals(auth.getName())) {
			return new ModelAndView("redirect:/errorpage");   
		}
		
		mav.setViewName("createContent");
		if(!personService.existsByEmail(email)) {
			return new ModelAndView("redirect:/errorpage");
		};
		
		Space space = personService.findByEmail(email).getSpace();
		
		if(space.getContents().size() >= MAX_CONTENT_NUM) {
			return new ModelAndView("redirect:/space");
		}
		
		mav.addObject("formModel", content);
		
		return mav;
	}
	
	/**
	 * <pre>
	 *  - 기능: createContent가 반환한 뷰에 사용자가 프로젝트 정보들을 입력하고 POST 전송하여 이를 해당 사용자의 프로젝트로 추가하기 위한 컨트롤러
	 *  - 반환: 
	 *  	- 성공
	 *  	View: 메인 홈으로 redirect
	 *  	DB: Content 테이블에 로우 추가
	 *  	- 실패
	 *   	로그인된 정보가 해당 포트폴리오 주인이 아닐때 -> 실패 페이지 반환
	 *   	일치하지만 데이터베이스에 저장되지 않은 이메일이면 실패 페이지 반환 -> 실제로 일어나지 않을 상황이지 않을까?
	 *   	Model 이 validation 통과 못함
	 *   	개인 최대 프로젝트 개수 초과 -> 실패 페이지 반환
	 *   </pre>
	 *   @param email 콘텐트 추가하려는 사람의 이메일 주소 
	 *   @param content 추가 되는 콘텐트 객체 
	 *   @author kimgun-yeong
	 */
	@RequestMapping(value="/space/makecontent/{email}", method = RequestMethod.POST)
	public ModelAndView createContentPost(@PathVariable String email,@Valid @ModelAttribute("formModel") Content content ,ModelAndView mav) {
		
		// 해당 스페이스가 현재 접속자의 것인지 확인하는 작업
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(!email.equals(auth.getName())) {
			// 접속자가 해당 컨텐츠의 주인이 아닐 때
			return new ModelAndView("redirect:/errorpage"); 
		}
		
		Person person = personService.findByEmail(email);
		if(person == null) {
			return new ModelAndView("redirect:/errorpage");
		}
		
		Space space = person.getSpace();
		
		if(space.getContents().size() >= MAX_CONTENT_NUM) {
			return new ModelAndView("redirect:/errorpage");
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
	 *		Model: formModel->ContentDTO(프로젝트 작성자 이메일 + 프로젝트 정보 DTO)
	 *		- 실패
	 *		입력된 id에 해당하는 content가 DB 테이블에 없을때 -> 실패 페이지 반환
	 *		현재 로그인 유저 != 해당 프로젝트 작성자 -> 실패 페이지 반환
	 * </pre>
	 * @param id 수정하려는 콘텐트의 id 값
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/space/updatecontent/{id}", method= RequestMethod.GET)
	public ModelAndView updateContent(@PathVariable long id, @ModelAttribute("formModel") ContentDTO contentDto, ModelAndView mav) {
		if(!contentService.existsById(id)) {
			return new ModelAndView("redirect:/errorpage");
		}
		Content content = contentService.findById(id);
		
		// 해당 컨텐트가 현재 접속자의 것인지 확인하는 작업
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String contentUserEmail = content.getSpace().getPerson().getEmail();
		
		if(!contentUserEmail.equals(auth.getName())) {
			return new ModelAndView("redirect:/errorpage"); 
		}
		
		contentDto.settingByEmailAndContent(contentUserEmail, content);
		mav.setViewName("updateContent");
		mav.addObject("formModel", contentDto);
		
		return mav;
	}
	
	/**
	 * <pre>
	 *  - 기능: updateContent가 반환한 뷰에서 수정한 Content 정보를 반영하기 위한 사용자의 POST Request를 담당하는 컨트롤러
	 *  - 반환:
	 *  	- 성공
	 * 		View: /space(현재 접속자의 포트폴리오) 로 redirect
	 * 		DB: content 변경 사항 save
	 * 		- 실패
	 * 		입력된 id에 해당하는 content가 DB 테이블에 없을때 -> 실패 페이지 반환
	 *		현재 로그인 유저 != 해당 프로젝트 작성자 -> 실패 페이지 반환
	 *	</pre>
	 * @param id 수정하려는 콘텐트의 id 값
	 * @param contentDto 콘텐트에 대한 수정 사항을 담은 DTO 객체
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/space/updatecontent/{id}", method= RequestMethod.POST) 
	public ModelAndView updateContentPost(@PathVariable long id, @ModelAttribute("formModel") ContentDTO contentDto, ModelAndView mav) {
		if(!contentService.existsById(id)) {
			return new ModelAndView("redirect:/errorpage");
		}
		Content content = contentService.findById(id);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String hostEmail = contentDto.getHostEmail();
		if(!hostEmail.equals(auth.getName())) {
			// 리퀘스트 보낸 사람이 이 콘텐츠의 주인과 다를때 
			return new ModelAndView("redirect:/errorpage");
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
		
		return new ModelAndView("redirect:/space");
	}
	
	/**
	 * <pre>
	 *  - 기능: url에 명시된 id의 콘텐트 삭제하는 컨트롤러
	 *  - 반환:
	 *  	- 성공
	 *  	View: /space(현재 접속자의 포트폴리오) 로 redirect
	 *  	DB: content 삭제
	 *  	- 실패
	 *  	입력된 id에 해당하는 content가 DB 테이블에 없을때 -> 실패 페이지 반환
	 *		현재 로그인 유저 != 해당 프로젝트 작성자 -> 실패 페이지 반환
	 *	</pre>
	 *	@param id 삭제하려는 콘텐트의 id 값
	 *	@author kimgun-yeong
	 */
	@RequestMapping(value="/space/deletecontent/{id}", method = RequestMethod.DELETE)
	public ModelAndView deleteContent(@PathVariable long id) {
		if(!contentService.existsById(id)) {
			return new ModelAndView("redirect:/errorpage");
		}
		Content targetContent = contentService.findById(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String hostEmail = targetContent.getSpace().getPerson().getEmail();
		if(!hostEmail.equals(auth.getName())) {
			// 리퀘스트 보낸 사람이 이 콘텐츠의 주인과 다를때 
			return new ModelAndView("redirect:/errorpage");
		}
		
		contentService.deleteContent(targetContent);
		return new ModelAndView("redirect:/space");
	}
}
