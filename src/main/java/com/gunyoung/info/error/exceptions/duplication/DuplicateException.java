package com.gunyoung.info.error.exceptions.duplication;

import com.gunyoung.info.error.exceptions.BusinessException;

@SuppressWarnings("serial")
public class DuplicateException extends BusinessException {

	public DuplicateException(String msg) {
		super(msg);
	}

}
