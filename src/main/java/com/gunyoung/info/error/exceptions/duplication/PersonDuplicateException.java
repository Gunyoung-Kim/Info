package com.gunyoung.info.error.exceptions.duplication;

/**
 * 이미 가입되어있는 유저가 중복으로 가입 요청하면 발생하는 예외 <br>
 * {@link DuplicateException} 상속
 * @author kimgun-yeong
 *
 */
@SuppressWarnings("serial")
public class PersonDuplicateException extends DuplicateException {

	public PersonDuplicateException(String msg) {
		super(msg);
	}

}
