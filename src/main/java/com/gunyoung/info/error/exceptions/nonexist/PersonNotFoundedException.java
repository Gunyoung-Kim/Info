package com.gunyoung.info.error.exceptions.nonexist;

@SuppressWarnings("serial")
public class PersonNotFoundedException extends NotFoundedException{

	public PersonNotFoundedException(String msg) {
		super(msg);
	}

}
