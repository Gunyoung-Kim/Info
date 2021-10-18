package com.gunyoung.info.services.email.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
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
class EmailServiceImplUnitTest {
	
	@Mock
	JavaMailSender mailSender;
	
	@Mock
	Logger logger;
	
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
	@DisplayName("EmailDTO에 담긴 정보를 통해 email 전송 -> MimeMessage 생성 과정에서 예외 발생")
	void sendEmailMessagingException() throws MessagingException {
		//Given
		MimeMessage mimeMessage = mock(MimeMessage.class);
		doThrow(MessagingException.class).when(mimeMessage).setSubject(emailDTO.getSubject(), "utf-8");
		given(mailSender.createMimeMessage()).willReturn(mimeMessage);
		
		//When
		emailService.sendEmail(emailDTO);
		
		//Then
		then(logger).should(times(1)).debug(anyString(), any(MessagingException.class));
	}
	
	@Test
	@DisplayName("EmailDTO에 담긴 정보를 통해 email 전송 -> InternetAddress 객체 생성 과정에서 예외 발생")
	void sendEmailUnsupportedEncodingException(){
		// to -do
	}
	
	@Test
	@DisplayName("EmailDTO에 담긴 정보를 통해 email 전송 -> 정상, mailSender check")
	void sendEmailTestCheckMailSender() {
		//Given
		MimeMessage mimeMessage = mock(MimeMessage.class);
		given(mailSender.createMimeMessage()).willReturn(mimeMessage);
		
		//When
		emailService.sendEmail(emailDTO);
		
		//Then
		then(mailSender).should(times(1)).send(any(MimeMessage.class));
	}
	
	@Test
	@DisplayName("EmailDTO에 담긴 정보를 통해 email 전송 -> 정상, logger check")
	void sendEmailTestCheckLogger() {
		//Given
		MimeMessage mimeMessage = mock(MimeMessage.class);
		given(mailSender.createMimeMessage()).willReturn(mimeMessage);
		
		//When
		emailService.sendEmail(emailDTO);
		
		//Then
		then(logger).should(times(1)).info("Email Send for {} to {} ", emailDTO.getSubject(), emailDTO.getReceiveMail());
	}
}
