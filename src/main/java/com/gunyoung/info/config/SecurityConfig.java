package com.gunyoung.info.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import com.gunyoung.info.security.UserAuthenticationProvider;
import com.gunyoung.info.services.social.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
    private UserAuthenticationProvider authenticationProvider;
	
	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.cors().and()
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/css/**", "/js/**", "/img/**").permitAll()
			.antMatchers("/space/makecontent/**","/space/updateprofile","/space","/space/updatecontent/**","/withdraw").hasAnyRole("USER")
			.antMatchers("/oauth2/join").hasAnyRole("PRE")
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
	
	// for thymeleaf <sec:authorize>
	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}
	
	
	@Override 
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth.authenticationProvider(authenticationProvider);
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
