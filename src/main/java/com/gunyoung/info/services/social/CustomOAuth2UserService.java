package com.gunyoung.info.services.social;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.gunyoung.info.dto.oauth2.OAuth2Attributes;
import com.gunyoung.info.services.domain.PersonService;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{

	@Autowired
	PersonService personService;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		
		String userNameAttributeName = userRequest.getClientRegistration()
												  .getProviderDetails()
												  .getUserInfoEndpoint()
												  .getUserNameAttributeName();
		
		OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
		
		if(!personService.existsByEmail(attributes.getEmail())) {
			return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_PRE")),attributes.getAttributes(),"email");
		}
		
		return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),attributes.getAttributes(),"email");
	}


}
