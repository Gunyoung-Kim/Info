package com.gunyoung.info.services.email;

import com.gunyoung.info.dto.email.EmailDTO;

public interface EmailService {
	public void sendEmail(EmailDTO email);
}
