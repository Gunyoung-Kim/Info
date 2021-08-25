package com.gunyoung.info.services.social.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
	
	@InjectMocks
	CustomOAuth2UserService customOAuth2UserService;
	
	/*
	 * public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
	 */
	
	@Test
	@DisplayName("DefaultOAuth2UserService를 사용하여 OAuth2User 로드하여 이 객체의 정보를 통해 비즈니스 요구사항에 맞게 새로운 DefaultOAuth2User 객체 생성 -> 이미 회원가입 유저")
	public void loadUserTestAlreadyExistPerson() {
		//Given
		String existEmail = "test@test.com";
		
		//When
		
		//Then
	}
	
	@Test
	@DisplayName("DefaultOAuth2UserService를 사용하여 OAuth2User 로드하여 이 객체의 정보를 통해 비즈니스 요구사항에 맞게 새로운 DefaultOAuth2User 객체 생성 -> 이미 회원가입 유저")
	public void loadUserTestNewPerson() {
		//Given
		String newPersonEmail = "newby@test.com";
		
		//When
		
		//Then
	}
}

