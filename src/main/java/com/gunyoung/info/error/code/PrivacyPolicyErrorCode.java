package com.gunyoung.info.error.code;

import lombok.Getter;

@Getter
public enum PrivacyPolicyErrorCode {
	PRIVACY_POLICY_VERSION_IS_NOT_VALID_ERROR("PP001","Such version of privacy policy is not founded")
	;
	private String code;
	private String description;
	private PrivacyPolicyErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
}
