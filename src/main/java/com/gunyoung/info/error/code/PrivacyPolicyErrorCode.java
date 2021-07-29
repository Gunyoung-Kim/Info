package com.gunyoung.info.error.code;

import lombok.Getter;

/**
 * 개인 정보 처리방침 관련 에러 코드
 * @author kimgun-yeong
 *
 */
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
