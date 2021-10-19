package com.gunyoung.info.services.email;

import com.gunyoung.info.dto.email.EmailDTO;

public interface EmailService {
	
	/**
	 * {@link EmailDTO}에 담긴 정보를 통해 email 전송 <br>
	 * 비동기로 실행 <br>
	 * 메시지 생성중간에 예외 발생 시 메일 발송하지 않고 로그로 남
	 * @author kimgun-yeong
	 */
	public void sendEmail(EmailDTO email);
}
