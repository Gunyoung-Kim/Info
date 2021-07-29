package com.gunyoung.info.error.exceptions.access;

/**
 * 접속자가 타인의 리소스에 접근하려할때 발생하는 예외 <br>
 * {@link PermissionDeniedException} 상속
 * @author kimgun-yeong
 *
 */
@SuppressWarnings("serial")
public class NotMyResourceException extends PermissoinDeniedException {
	public NotMyResourceException(String msg) {
		super(msg);
	}
}
