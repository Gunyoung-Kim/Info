package com.gunyoung.info.dto.email;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailDTO {
	private String senderName;
	private String senderMail;
	private String receiveMail;
	private String subject;
	private String message;
}
