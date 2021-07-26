package com.gunyoung.info.error.exceptions.access;

import com.gunyoung.info.error.exceptions.BusinessException;

@SuppressWarnings("serial")
public class PermissoinDeniedException extends BusinessException {

	public PermissoinDeniedException(String msg) {
		super(msg);
	}

}
