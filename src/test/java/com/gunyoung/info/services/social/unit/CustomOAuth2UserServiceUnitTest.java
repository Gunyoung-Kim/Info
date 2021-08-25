package com.gunyoung.info.services.social.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.services.social.CustomOAuth2UserService;

/**
 * {@link CustomOAuth2UserService} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) CustomOAuth2UserService only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class CustomOAuth2UserServiceUnitTest {
	
	@Mock
	PersonService personService;
	
	@Mock
	DefaultOAuth2UserService defaultOAuth2UserService;
	
	@InjectMocks
	CustomOAuth2UserService customOAuth2UserService;
	
	/*
	 * public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
	 */
	
	@Test
	@DisplayName("DefaultOAuth2UserService를 사용하여 OAuth2User 로드하여 이 객체의 정보를 통해 비즈니스 요구사항에 맞게 새로운 DefaultOAuth2User 객체 생성 -> 구글, 이미 회원가입 유저")
	public void loadUserTestGoogleAlreadyExistPerson() {
		//Given
		String existEmail = "test@test.com";
		String personName = "person";
		Map<String, Object> userAttributes = new HashMap<>();
		userAttributes.put("sub", 12342535);
		userAttributes.put("name", personName);
		userAttributes.put("email", existEmail);
		OAuth2User oAuth2UserFromDefaultOAuth2UserService = new DefaultOAuth2User(null, userAttributes, "sub");
		
		OAuth2UserRequest userRequest = mock(OAuth2UserRequest.class);
		given(defaultOAuth2UserService.loadUser(userRequest)).willReturn(oAuth2UserFromDefaultOAuth2UserService);
		mockingUserRequestGetClientRegistration(userRequest, "google");
		
		//When
		OAuth2User result = customOAuth2UserService.loadUser(userRequest);
		
		//Then
		result.getAuthorities().stream().anyMatch((a) -> {
			return a.getAuthority().equals("ROLE_USER");
		});
	}
	
	@Test
	@DisplayName("DefaultOAuth2UserService를 사용하여 OAuth2User 로드하여 이 객체의 정보를 통해 비즈니스 요구사항에 맞게 새로운 DefaultOAuth2User 객체 생성 -> 구글, 이미 회원가입 유저")
	public void loadUserTestGoogleNewPerson() {
		//Given
		String newPersonEmail = "newby@test.com";
		String personName = "person";
		Map<String, Object> userAttributes = new HashMap<>();
		userAttributes.put("sub", 12342535);
		userAttributes.put("name", personName);
		userAttributes.put("email", newPersonEmail);
		OAuth2User oAuth2UserFromDefaultOAuth2UserService = new DefaultOAuth2User(null, userAttributes, "sub");
		
		OAuth2UserRequest userRequest = mock(OAuth2UserRequest.class);
		given(defaultOAuth2UserService.loadUser(userRequest)).willReturn(oAuth2UserFromDefaultOAuth2UserService);
		mockingUserRequestGetClientRegistration(userRequest, "google");
		
		//When
		OAuth2User result = customOAuth2UserService.loadUser(userRequest);
		
		//Then
		result.getAuthorities().stream().anyMatch((a) -> {
			return a.getAuthority().equals("ROLE_PRE");
		});
	}
	
	private void mockingUserRequestGetClientRegistration(OAuth2UserRequest userRequest, String registrationId) {
		ClientRegistration clientRegistration = ClientRegistration.withRegistrationId(registrationId)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.clientId("7226-ttlq0ofr8o5.apps.testusercontent.com")
				.tokenUri("test.com")
				.build();
		given(userRequest.getClientRegistration()).willReturn(clientRegistration);
	}
}

