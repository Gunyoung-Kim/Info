package com.gunyoung.info.services.email;

import java.io.UnsupportedEncodingException;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.gunyoung.info.dto.email.EmailDTO;

import lombok.RequiredArgsConstructor;

@Service("emailService")
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
	
	private static final String CHAR_SET_FOR_MESSAGE = "utf-8";

	private final JavaMailSender mailSender;
	
	private final Logger logger;
	
	@Override
	@Async
	public void sendEmail(EmailDTO email){
		try {
			MimeMessage msg = mailSender.createMimeMessage();
			
			msg.addRecipient(RecipientType.TO, new InternetAddress(email.getReceiveMail()));
			msg.addFrom(new InternetAddress[] {
					new InternetAddress(email.getSenderMail(), email.getSenderName())
			});
			msg.setSubject(email.getSubject(), CHAR_SET_FOR_MESSAGE);
			msg.setText(email.getMessage(), CHAR_SET_FOR_MESSAGE);
			mailSender.send(msg);
			logger.info("Email Send for {} to {} ", email.getSubject(), email.getReceiveMail());
		} catch(MessagingException e) {
			logger.debug("MessagingException occurred while setting MimeMessage", e);
		} catch (UnsupportedEncodingException e) {
			logger.debug("UnsupportedEncodingException occurred while creating InternetAddress", e);
		}	
	}
}

