package com.gunyoung.info.error.exceptions.nonexist;

/**
 * 요청으로 들어온 개인정보 처리방침이 없을때 발생 <br>
 * {@link NotFoundedException} 상속
 * @author kimgun-yeong
 *
 */
@SuppressWarnings("serial")
public class PrivacyPolicyNotFoundedException extends NotFoundedException {

	public PrivacyPolicyNotFoundedException(String msg) {
		super(msg);
	}

}
