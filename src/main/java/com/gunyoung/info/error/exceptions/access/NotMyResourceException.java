package com.gunyoung.info.error.exceptions.access;

@SuppressWarnings("serial")
public class NotMyResourceException extends PermissoinDeniedException {

	public NotMyResourceException(String msg) {
		super(msg);
	}

}
