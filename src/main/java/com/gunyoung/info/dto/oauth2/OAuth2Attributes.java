package com.gunyoung.info.dto.oauth2;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2Attributes {
	private Map<String, Object> attributes;
	private String email;
	private String nameAttributeKey;
	private String name;
	
	/**
	 * registrationId 에 따라 어떤 of+소셜서비스이름 메소드를 호출할 것인지 결정
	 * @author kimgun-yeong
	 */
	public static OAuth2Attributes of(String registrationId, String userNameAttributeName,Map<String, Object> attributes) {
		return ofGoogle(userNameAttributeName, attributes);
	}
	
	/**
	 * Google 소셜서비스를 통해 로그인한 유저의 OAuth2Attributes 반환
	 * @author kimgun-yeong
	 */
	private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuth2Attributes.builder()
				.name((String) attributes.get("name"))
				.email((String) attributes.get("email"))
				.attributes(attributes)
				.nameAttributeKey(userNameAttributeName)
	            .build();
	}
}
