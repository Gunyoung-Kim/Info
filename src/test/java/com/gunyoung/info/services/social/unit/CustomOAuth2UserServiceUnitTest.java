package com.gunyoung.info.services.social.unit;

import org.junit.jupiter.api.extension.ExtendWith;
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
	
	/*
	 * public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
	 */
}

