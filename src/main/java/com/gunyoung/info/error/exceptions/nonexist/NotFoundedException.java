package com.gunyoung.info.error.exceptions.nonexist;

import com.gunyoung.info.error.exceptions.BusinessException;

@SuppressWarnings("serial")
public class NotFoundedException extends BusinessException {

	public NotFoundedException(String msg) {
		super(msg);
	}

}
