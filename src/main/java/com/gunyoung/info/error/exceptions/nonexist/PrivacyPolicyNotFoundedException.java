package com.gunyoung.info.error.exceptions.nonexist;

@SuppressWarnings("serial")
public class PrivacyPolicyNotFoundedException extends NotFoundedException {

	public PrivacyPolicyNotFoundedException(String msg) {
		super(msg);
	}

}
