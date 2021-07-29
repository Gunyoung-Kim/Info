package com.gunyoung.info.error.exceptions.access;

import com.gunyoung.info.error.exceptions.BusinessException;

/**
 * 클라이언트가 권한이 없는 요청할때 발생하는 예외들의 부모 클래스 <br>
 * {@link BusinessException} 상속
 * @author kimgun-yeong
 *
 */
@SuppressWarnings("serial")
public class PermissoinDeniedException extends BusinessException {

	public PermissoinDeniedException(String msg) {
		super(msg);
	}

}
