package com.gunyoung.info.error.exceptions.duplication;

import com.gunyoung.info.error.exceptions.BusinessException;

/**
 * Uniqueness가 보장되어야 하는 리소스에 대해 이를 위협하는 요청에 발생하는 예외들의 부모 클래스 <br>
 * {@link BusinessException} 상속 
 * @author kimgun-yeong
 *
 */
@SuppressWarnings("serial")
public class DuplicateException extends BusinessException {

	public DuplicateException(String msg) {
		super(msg);
	}

}
