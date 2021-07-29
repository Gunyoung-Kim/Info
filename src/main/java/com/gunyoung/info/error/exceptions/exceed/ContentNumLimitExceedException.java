package com.gunyoung.info.error.exceptions.exceed;

/**
 * 개인에게 할당된 최대 Content 개수 초과하려는 요청 시 발생하는 예외 <br>
 * {@link ExceedException} 상속
 * @author kimgun-yeong
 *
 */
@SuppressWarnings("serial")
public class ContentNumLimitExceedException extends ExceedException {

	public ContentNumLimitExceedException(String msg) {
		super(msg);
	}

}
