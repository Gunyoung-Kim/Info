package com.gunyoung.info.error.exceptions.duplication;

@SuppressWarnings("serial")
public class PersonDuplicateException extends DuplicateException {

	public PersonDuplicateException(String msg) {
		super(msg);
	}

}
