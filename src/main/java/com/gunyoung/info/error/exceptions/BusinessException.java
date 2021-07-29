package com.gunyoung.info.error.exceptions;

/**
 * 클라이언트 요청 처리 과정에서 발생하는 예외들에 부모 클래스 <br>
 * 언체크 예외로서 {@link RuntimeException} 상속
 * @author kimgun-yeong
 *
 */
@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {
	public BusinessException(String msg) {
		super(msg);
	}
}
