package com.gunyoung.info.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 전용 설정 클래스
 * @author kimgun-yeong
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer{
	@Bean 
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource rbms = new ReloadableResourceBundleMessageSource();
		rbms.addBasenames("classpath:validationMessage_ko");
		rbms.setDefaultEncoding("UTF-8");
		return rbms;
	}
	
	@Bean
	public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource);
		return validator;
	}
}
