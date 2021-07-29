package com.gunyoung.info.error.exceptions.nonexist;

/**
 * DB에 없는 Content에 대한 요청에 발생하는 예외 <br>
 * {@link NotFoundedException} 상속
 * @author kimgun-yeong
 *
 */
@SuppressWarnings("serial")
public class ContentNotFoundedException extends NotFoundedException {

	public ContentNotFoundedException(String msg) {
		super(msg);
	}

}
