package com.gunyoung.info.service.unit;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import com.gunyoung.info.dto.email.EmailDTO;
import com.gunyoung.info.services.email.EmailServiceImpl;

/**
 * {@link EmailServiceImpl}에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) Service class only <br>
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class EmailServiceUnitTest {
	
	@Mock
	JavaMailSender mailSender;
	
	@InjectMocks
	EmailServiceImpl emailService;
	
	/** 
	 *  - 대상 메소드:
	 *  	public void sendEmail(EmailDTO email); 
	 *  @author kimgun-yeong
	 */
	@Test
	public void sendEmailTest() {
		//Given
		EmailDTO email = EmailDTO.builder()
								 .senderMail("test@google.com")
								 .senderName("test")
								 .receiveMail("test@naver.com")
								 .subject("subject")
								 .message("message")
								 .build();
		
		//When
		emailService.sendEmail(email);
		
		//Then
		then(mailSender).should(times(1)).createMimeMessage();
	}
}
