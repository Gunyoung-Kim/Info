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
