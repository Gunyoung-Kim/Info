package com.gunyoung.info.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import com.gunyoung.info.dto.email.EmailDTO;
import com.gunyoung.info.services.email.EmailServiceImpl;

public class EmailServiceTest {
	
	/** 
	 *  - 대상 메소드:
	 *  	public void sendEmail(EmailDTO email); 
	 *  @author kimgun-yeong
	 */
	@Test
	public void sendEmailTest() {
		EmailServiceImpl emailServiceImpl = new EmailServiceImpl();
		
		JavaMailSender mockMailSender = mock(JavaMailSender.class);
		
		emailServiceImpl.setMailSender(mockMailSender);
		
		EmailDTO email = EmailDTO.builder()
								 .senderMail("test@google.com")
								 .senderName("test")
								 .receiveMail("test@naver.com")
								 .subject("subject")
								 .message("message")
								 .build();
		
		emailServiceImpl.sendEmail(email);
		
		verify(mockMailSender, times(1)).createMimeMessage();
	}
}
