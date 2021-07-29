package com.gunyoung.info.error.exceptions.nonexist;

import com.gunyoung.info.error.exceptions.BusinessException;

/**
 * 요청 처리에 있어 필요한 개체가 없을 때 발생하는 예외 <br>
 * {@link BusinessException} 상속
 * @author kimgun-yeong
 *
 */
@SuppressWarnings("serial")
public class NotFoundedException extends BusinessException {

	public NotFoundedException(String msg) {
		super(msg);
	}

}
