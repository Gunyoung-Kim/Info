package com.gunyoung.info.dto.email;

import lombok.Builder;
import lombok.Data;

/**
 * 서버측에서 사용자에게 이메일을 보낼 때 사용되는 DTO 객체
 * @author kimgun-yeong
 *
 */
@Data
@Builder
public class EmailDTO {
	private String senderName;
	private String senderMail;
	private String receiveMail;
	private String subject;
	private String message;
}
