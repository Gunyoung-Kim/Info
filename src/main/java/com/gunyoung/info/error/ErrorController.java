package com.gunyoung.info.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gunyoung.info.error.code.ContentErrorCode;
import com.gunyoung.info.error.code.PersonErrorCode;
import com.gunyoung.info.error.exceptions.access.NotMyResourceException;
import com.gunyoung.info.error.exceptions.duplication.PersonDuplicateException;
import com.gunyoung.info.error.exceptions.exceed.ContentNumLimitExceedException;
import com.gunyoung.info.error.exceptions.nonexist.ContentNotFoundedException;
import com.gunyoung.info.error.exceptions.nonexist.PersonNotFoundedException;

@RestControllerAdvice
public class ErrorController {
	
	/*
	 *  ---------------------- NO CONTENT ---------------------------------------------------
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
	
	/*
	 * ---------------------- CONFLICT ---------------------------------------------------
	 */
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(PersonDuplicateException.class)
	public ErrorMsg personDuplicated(PersonDuplicateException e) {
		return new ErrorMsg(PersonErrorCode.PERSON_DUPLICATION_FOUNDED_ERROR.getCode(),e.getMessage());
	}
	
	/*
	 * ---------------------- BAD REQUEST ---------------------------------------------------
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
