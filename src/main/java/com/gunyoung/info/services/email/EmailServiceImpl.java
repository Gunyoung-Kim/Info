package com.gunyoung.info.services.email;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.gunyoung.info.dto.email.EmailDTO;

import lombok.Setter;

@Service("emailService")
@Setter
public class EmailServiceImpl implements EmailService{

	@Autowired
	JavaMailSender mailSender;
	
	@Override
	public void sendEmail(EmailDTO email) {
		try {
			MimeMessage msg = mailSender.createMimeMessage();
			
			msg.addRecipient(RecipientType.TO, new InternetAddress(email.getReceiveMail()));
			msg.addFrom(new InternetAddress[] {
					new InternetAddress(email.getSenderMail(),email.getSenderName())
			});
			msg.setSubject(email.getSubject(), "utf-8");
			msg.setText(email.getMessage(),"utf-8");
			mailSender.send(msg);
		} catch(Exception ex) {
		}
	}
	
}
