package com.gunyoung.info.error.exceptions.nonexist;

@SuppressWarnings("serial")
public class ContentNotFoundedException extends NotFoundedException {

	public ContentNotFoundedException(String msg) {
		super(msg);
	}

}
