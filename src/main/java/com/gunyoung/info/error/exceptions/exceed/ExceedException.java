package com.gunyoung.info.error.exceptions.exceed;

import com.gunyoung.info.error.exceptions.BusinessException;

/**
 * 특정 리소스의 최대 개수를 초과하는 요청에 발생하는 예외 <br>
 * {@link BusinessException} 상속
 * @author kimgun-yeong
 *
 */
@SuppressWarnings("serial")
public class ExceedException extends BusinessException {

	public ExceedException(String msg) {
		super(msg);
	}

}
