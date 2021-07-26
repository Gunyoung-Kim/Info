package com.gunyoung.info.error.exceptions.exceed;

import com.gunyoung.info.error.exceptions.BusinessException;

@SuppressWarnings("serial")
public class ExceedException extends BusinessException {

	public ExceedException(String msg) {
		super(msg);
	}

}
