package com.gunyoung.info.services.social;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.gunyoung.info.dto.oauth2.OAuth2Attributes;
import com.gunyoung.info.services.domain.PersonService;

import lombok.RequiredArgsConstructor;

/**
 * {@link DefaultOAuth2UserService}를 사용하지만 비즈니스 요구사항에 맞게 일부 커스터마이징하는 서비스
 * @author kimgun-yeong
 *
 */
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final PersonService personService;
	
	private final DefaultOAuth2UserService defaultOAuth2UserService;
	
	/**
	 * {@link DefaultOAuth2UserService}를 사용하여 {@link OAuth2User} 로드하여 이 객체의 정보를 통해 비즈니스 요구사항에 맞게 새로운 {@link DefaultOAuth2User} 객체 생성
	 * @author kimgun-yeong
	 */
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
		
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		
		String userNameAttributeName = userRequest.getClientRegistration()
												  .getProviderDetails()
												  .getUserInfoEndpoint()
												  .getUserNameAttributeName();
		
		OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
		if(personService.existsByEmail(attributes.getEmail())) {
			return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),attributes.getAttributes(),"email");
		}
		return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_PRE")),attributes.getAttributes(),"email");
	}


}
