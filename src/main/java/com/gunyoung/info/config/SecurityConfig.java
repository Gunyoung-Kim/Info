package com.gunyoung.info.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import com.gunyoung.info.security.UserAuthenticationProvider;
import com.gunyoung.info.services.social.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

/**
 * Spring Security 설정을 위한 Configuration 클래스 (WebSecurityConfigurerAdapter 상속)
 * @author kimgun-yeong
 *
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final UserDetailsService userDetailsService;
	
	
	private final CustomOAuth2UserService customOAuth2UserService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.cors().and()
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/css/**", "/js/**", "/img/**").permitAll()
			.antMatchers("/space/makecontent/**","/space/updateprofile","/space","/space/updatecontent/**","/withdraw").hasAnyRole("USER","ADMIN")
			.antMatchers("/oauth2/join").hasAnyRole("PRE","ADMIN")
			.anyRequest()
			.permitAll();
			
		http.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/")
			.permitAll();
		
		http.oauth2Login()
			.loginPage("/login")
			.userInfoEndpoint()
			.userService(customOAuth2UserService);
			
		http.logout()
			.logoutSuccessUrl("/")
			.permitAll();
	}
	
	/**
	 * @return RoleHierarchy 객체 - 유저 권환 계급 체계 반환 
	 * @author kimgun-yeong
	 */
	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		
		Map<String,List<String>> roleHierarchyMap = new HashMap<>();
		roleHierarchyMap.put("ROLE_ADMIN", Arrays.asList("ROLE_USER"));
		
		String roles = RoleHierarchyUtils.roleHierarchyFromMap(roleHierarchyMap);
	
		roleHierarchy.setHierarchy(roles);
		return roleHierarchy;
	}
	
	@Bean
	public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
		DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
		webSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
		return webSecurityExpressionHandler;
	}
	
	
	/**
	 * Thymeleaf에서 <sec:authorize> 네임 스페이스 같은 확장 기능을 사용하기 위한 Bean 
	 * @return 
	 * @author kimgun-yeong
	 */
	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}
	
	@Bean
	public UserAuthenticationProvider authenticationProvider() {
		return new UserAuthenticationProvider(userDetailsService,passwordEncoder());
	}
	
	
	@Override 
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth.authenticationProvider(authenticationProvider());
	}
	
	
	// for password Encoding
	@Bean 
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/*
	@Bean
	public SecurityAuthenticationFilter securityAuthenticationFilter() {
		return new SecurityAuthenticationFilter();
	}
	*/
	
}
