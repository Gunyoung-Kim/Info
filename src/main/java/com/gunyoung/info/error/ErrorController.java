package com.gunyoung.info.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gunyoung.info.error.code.ContentErrorCode;
import com.gunyoung.info.error.code.PersonErrorCode;
import com.gunyoung.info.error.code.PrivacyPolicyErrorCode;
import com.gunyoung.info.error.exceptions.access.NotMyResourceException;
import com.gunyoung.info.error.exceptions.duplication.PersonDuplicateException;
import com.gunyoung.info.error.exceptions.exceed.ContentNumLimitExceedException;
import com.gunyoung.info.error.exceptions.nonexist.ContentNotFoundedException;
import com.gunyoung.info.error.exceptions.nonexist.PersonNotFoundedException;
import com.gunyoung.info.error.exceptions.nonexist.PrivacyPolicyNotFoundedException;

/**
 * {@code @RestControllerAdvice} = {@code @ResponseBody} +{@code @ControllerAdvice}
 * Controller 에서 예외 발생시 AOP를 통해 예외 처리 및 클라이언트에 에러 메시지 반환하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@RestControllerAdvice
public class ErrorController {
	
	/**
	 *  ---------------------- NO CONTENT ---------------------------------------------------
	 *  주로 DB에서 주어진 조건으로 찾은 결과 값이 없을 때 발생 
	 *  @author kimgun-yeong
	 */
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(PersonNotFoundedException.class)
	public ErrorMsg personNotFounded(PersonNotFoundedException e) {
		return new ErrorMsg(PersonErrorCode.PERSON_NOT_FOUNDED_ERROR.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(ContentNotFoundedException.class)
	public ErrorMsg contentNotFounded(ContentNotFoundedException e) {
		return new ErrorMsg(ContentErrorCode.CONTENT_NOT_FOUNDED_ERROR.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(PrivacyPolicyNotFoundedException.class)
	public ErrorMsg privacyPolicyNotFounded(PrivacyPolicyNotFoundedException e) {
		return new ErrorMsg(PrivacyPolicyErrorCode.PRIVACY_POLICY_VERSION_IS_NOT_VALID_ERROR.getCode(),e.getMessage());
	}
	
	/**
	 * ---------------------- CONFLICT ---------------------------------------------------
	 * 주로 리소스 추가하는 과정에 있어 리소스의 uniqueness 를 위반하는 요청일때 발생 
	 * @author kimgun-yeong 
	 */
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(PersonDuplicateException.class)
	public ErrorMsg personDuplicated(PersonDuplicateException e) {
		return new ErrorMsg(PersonErrorCode.PERSON_DUPLICATION_FOUNDED_ERROR.getCode(),e.getMessage());
	}
	
	/**
	 * ---------------------- BAD REQUEST ---------------------------------------------------
	 * 주로 접속자의 것이 아닌 리소스에 접근을 하거나 리소스 제한 개수를 초과할떄 발생
	 * @author kimgun-yeong
	 */
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ContentNumLimitExceedException.class)
	public ErrorMsg contentNumLimitExceeded(ContentNumLimitExceedException e) {
		return new ErrorMsg(ContentErrorCode.CONTENT_NUM_LIMIT_EXCEEDED_ERROR.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NotMyResourceException.class)
	public ErrorMsg notMyResource(NotMyResourceException e) {
		return new ErrorMsg(PersonErrorCode.RESOURCE_IS_NOT_MINE_ERROR.getCode(),e.getMessage());
	}
}
