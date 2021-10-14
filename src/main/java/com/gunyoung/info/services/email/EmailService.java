package com.gunyoung.info.services.email;

import com.gunyoung.info.dto.email.EmailDTO;

public interface EmailService {
	
	/**
	 * {@link EmailDTO}에 담긴 정보를 통해 email 전송 <br>
	 * 비동기로 실행
	 * @author kimgun-yeong
	 */
	public void sendEmail(EmailDTO email);
}
