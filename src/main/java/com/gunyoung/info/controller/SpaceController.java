package com.gunyoung.info.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.dto.ProfileDTO;
import com.gunyoung.info.error.code.PersonErrorCode;
import com.gunyoung.info.error.exceptions.nonexist.PersonNotFoundedException;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.util.AuthorityUtil;

import lombok.RequiredArgsConstructor;

/**
 * Space 도메인 관련 화면 반환 컨트롤러 클래스 
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class SpaceController {
	
	private final PersonService personService;
	
	private final ContentService contentService;
	
	/**
	 * <pre>
	 *  - 기능: 현재 로그인되있는 사용자 본인의 포트폴리오 페이지 반환
	 *  - 반환: 
	 *  	- 성공
	 *  	View: portfolio.html (현재 로그인 유저의 포트폴리오), login.html(로그인 안되있으면)
	 *  	- 실패 
	 *  </pre>
	 *  @author kimgun-yeong
	 */
	@RequestMapping(value="/space", method= RequestMethod.GET)
	public ModelAndView myspaceView() {
		String loginUserEmail = AuthorityUtil.getSessionUserEmail();
		Person loginUser = personService.findByEmail(loginUserEmail);
		if(loginUser == null) {
			return new ModelAndView("redirect:/login");
		}
		Long loginUserId = loginUser.getId();
		
		return new ModelAndView("redirect:/space/"+ loginUserId);
	}
	
	/**
	 * <pre>
	 *  - 기능: 개인 포트폴리오 페이지 반환
	 *  - 반환:
	 *  	- 성공
	 *  	View: portfolio.html (url에 입력된 이메일 유저의 포트폴리오)
	 *  	Model: profile -> ProfileDTO (포트폴리오 주인의 프로필 정보를 전달하는 DTO- Person+Space 일부 필드)
	 *  		   contents -> List<Content> (포트폴리오에 있는 프로젝트 리스트)
	 *  		   isHost -> boolean (현재 로그인된 유저가 해당 포트폴리오의 주인인지 여부-> 템플릿에 변화 주기위함(ex. 프로젝트 수정 버튼 추가))
	 *  </pre>
	 *  @param userId 열람하려는 포트폴리오 주인의 Id
	 *  @throws PersonNotFoundedException url에 입력된 id의 Person DB에 없으면
	 *  @author kimgun-yeong
	 */
	@RequestMapping(value="/space/{userId}", method= RequestMethod.GET)
	public ModelAndView spaceView(@PathVariable Long userId, ModelAndView mav) {
		Person spaceHost = personService.findByIdWithSpace(userId);
		if(spaceHost == null) {
			throw new PersonNotFoundedException(PersonErrorCode.PERSON_NOT_FOUNDED_ERROR.getDescription());
		}
		
		Space space = spaceHost.getSpace();
		ProfileDTO profileDTO = ProfileDTO.createFromPersonAndSpace(spaceHost, space);
		
		Long spaceId = space.getId();
		List<Content> contents = contentService.findAllBySpaceIdWithLinks(spaceId);
		
		boolean isSessionUserHost = getIsSessionUserHost(spaceHost.getEmail());
		
		mav.addObject("profile", profileDTO);
		mav.addObject("contents",contents);
		mav.addObject("isHost", isSessionUserHost);
		
		mav.setViewName("portfolio");
		
		return mav;
	}
	
	private boolean getIsSessionUserHost(String spaceHostEmail) {
		String loginUserEmail = AuthorityUtil.getSessionUserEmail();
		return loginUserEmail.equals(spaceHostEmail);
	}
	
	/** 
	 * <pre>
	 *  - 기능: 현재 로그인한 유저의 프로필을 변경하기 위한 뷰를 반환
	 *  - 반환: 
	 *  	- 성공
	 *  	View: updateProfile.html (프로필 업데이트 사항 작성을 위한 템플릿)
	 *  	Model: formModel->ProfileDTO(프로필 업데이트 사항 전달을 위한 DTO객체, Person+ Space 일부필드)
	 *  </pre>
	 *  @throws PersonNotFoundedException 현재 로그인된 유저의 이메일이 DB에 없으면
	 *  @author kimgun-yeong
	 */
	@RequestMapping(value="/space/updateprofile", method = RequestMethod.GET)
	public ModelAndView updateProfileView(ModelAndView mav) {
		String userEmail = AuthorityUtil.getSessionUserEmail();
		Person user = personService.findByEmailWithSpace(userEmail);
		if(user == null) {
			throw new PersonNotFoundedException(PersonErrorCode.PERSON_NOT_FOUNDED_ERROR.getDescription());
		}
		
		Space space = user.getSpace();
		ProfileDTO profileDTO = ProfileDTO.createFromPersonAndSpace(user, space);
		
		mav.addObject("formModel", profileDTO);
		
		mav.setViewName("updateProfile");
		
		return mav;
	}
}
