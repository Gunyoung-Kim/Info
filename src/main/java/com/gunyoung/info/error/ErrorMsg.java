package com.gunyoung.info.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 클라이언트의 요청 처리에 있어 예외 발생 시 예외 발생 원인을 클라이언트에게 알려주기 위해 사용되는 클래스
 * @author kimgun-yeong
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMsg {
	private String errorCode;
	private String description;
}
