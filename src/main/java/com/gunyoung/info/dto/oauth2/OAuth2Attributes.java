package com.gunyoung.info.dto.oauth2;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2Attributes {
	private Map<String, Object> attributes;
	private String email;
	private String nameAttributeKey;
	private String name;
	
	@Builder 
	public OAuth2Attributes(Map<String,Object> attributes, String email, String name, String nameAttributeKey) {
		this.attributes = attributes;
		this.nameAttributeKey= nameAttributeKey;
		this.name = name;
		this.email = email;
	}
	
	public static OAuth2Attributes of(String registrationId, String userNameAttributeName,Map<String, Object> attributes) {
		return ofGoogle(userNameAttributeName, attributes);
	}
	
	private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuth2Attributes.builder()
				.name((String) attributes.get("name"))
				.email((String) attributes.get("email"))
				.attributes(attributes)
				.nameAttributeKey(userNameAttributeName)
	            .build();
	}
}
