package com.gunyoung.info.controller.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * 무중단 배포를 위한 ProfileController
 * @author kimgun-yeong
 *
 */
@RestController
@RequiredArgsConstructor
public class ProfileController {
	
	public static final String DEFAULT_PROFILE = "default";
	
	private final Environment env;
	
	/**
	 * 무중단 배포를 위해 현 애플리케이션이 'server'를 prefix로 갖는 프로필이 어느 프로필인지 반환 <br>
	 * 'server'을 prefix로 갖는 프로필이 없다면 'default' 반환 
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/profile", method= RequestMethod.GET)
	public String getProfile() {
		List<String> profiles = Arrays.asList(env.getActiveProfiles());
		List<String> forNonStopProfiles = Arrays.asList("server1","server2");
		
		String defaultProfile = profiles.isEmpty() ? DEFAULT_PROFILE : profiles.get(0);
		
		return Arrays.stream(env.getActiveProfiles())
				.filter(forNonStopProfiles::contains)
				.findAny()
				.orElse(defaultProfile);
	}
	
}
