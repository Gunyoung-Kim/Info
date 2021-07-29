package com.gunyoung.info.error.code;

import lombok.Getter;

/**
 * Content 도메인 관련 에러코드
 * @author kimgun-yeong
 *
 */
@Getter
public enum ContentErrorCode {
	CONTENT_NOT_FOUNDED_ERROR("C001","Content not founded"),
	CONTENT_NUM_LIMIT_EXCEEDED_ERROR("C002","Over the allocation count of content")
	;
	private String code;
	private String description;
	private ContentErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
}
