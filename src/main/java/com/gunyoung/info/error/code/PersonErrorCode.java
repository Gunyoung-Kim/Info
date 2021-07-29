package com.gunyoung.info.error.code;

import lombok.Getter;

/**
 * Person 도메인 관련 에러 코드
 * @author kimgun-yeong
 *
 */
@Getter
public enum PersonErrorCode {
	PERSON_NOT_FOUNDED_ERROR("P001","Person is not founded"),
	PERSON_DUPLICATION_FOUNDED_ERROR("P002","Person duplication is founded"),
	RESOURCE_IS_NOT_MINE_ERROR("P003","Access denied because resource is not yours")
	;
	private String code;
	private String description;
	private PersonErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
}
