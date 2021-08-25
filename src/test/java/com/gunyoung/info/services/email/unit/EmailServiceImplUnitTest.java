package com.gunyoung.info.services.email.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import com.gunyoung.info.dto.email.EmailDTO;
import com.gunyoung.info.services.email.EmailServiceImpl;

/**
 * {@link EmailServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) EmailServiceImpl only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class EmailServiceImplUnitTest {
	
	@Mock
	JavaMailSender mailSender;
	
	@InjectMocks
	EmailServiceImpl emailService;
	
	private EmailDTO emailDTO;
	
	@BeforeEach
	void setup() {
		emailDTO = EmailDTO.builder()
				.senderMail("sender@test.com")
				.senderName("INFO")
				.receiveMail("receiver@test.com")
				.subject("subject")
				.message("message")
				.build();
	}
	
	/*
	 * public void sendEmail(EmailDTO email)
	 */
	
	@Test
	@DisplayName("EmailDTO에 담긴 정보를 통해 email 전송 -> 정상, mailSender check")
	public void sendEmailTestCheckMailSender() {
		//Given
		MimeMessage mimeMessage = mock(MimeMessage.class);
		given(mailSender.createMimeMessage()).willReturn(mimeMessage);
		
		//When
		emailService.sendEmail(emailDTO);
		
		//Then
		then(mailSender).should(times(1)).send(any(MimeMessage.class));
	}
}
