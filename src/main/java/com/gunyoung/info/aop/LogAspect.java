package com.gunyoung.info.aop;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.RequiredArgsConstructor;

/**
 *  로깅을 위한 클래스 
 * @author kimgun-yeong
 *
 */
@Component
@Aspect
@RequiredArgsConstructor
public class LogAspect {
	
	private final Logger logger;

	/**
	 * 컨트롤러에 Request 가 들어오면 Request 메소드,uri, parameters, remote address, 처리 시간 들을 로깅 하기 위한 어드바이스
	 * @author kimgun-yeong
	 */
	@Around("within(com.gunyoung.info.controller..*)")
	public Object loggingAroundController(ProceedingJoinPoint pjp) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		Map<String, String[]> paramMap = request.getParameterMap();
		String params = "";
		if(!paramMap.isEmpty()) {
			params = "[ " + paramMapToString(paramMap) + "]";
		}
		
		String remoteHost = getRemoteHostFromHttpRequest(request);
		
		long beforeProceedTime = System.currentTimeMillis();
		try {
			return pjp.proceed(pjp.getArgs());
		} finally {
			long afterProccedTime = System.currentTimeMillis();
			logger.info("Request: {} {}{} < {} ({}ms)",request.getMethod(), request.getRequestURI(), params, remoteHost, afterProccedTime - beforeProceedTime);
		}
	}
	
	/**
	 * HttpRequest의 RemoteHost를 반환하는 메소드 <br>
	 * 리버스 프록시인 Nginx의 사용으로 RemoteHost 값이 127.0.0.1(localhost)로 나타난다. <br>
	 * 기존의 RemoteHost 값은 X-Real-IP 헤더에 담겨져 있기에 이를 반영 <br>
	 * 리버스 프록시가 따로 없다면 기존의 RemoteHost 값 반환
	 * @author kimgun-yeong
	 */
	private String getRemoteHostFromHttpRequest(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader("X-Real-IP"))
				.orElse(request.getRemoteHost());
	}
	
	/**
	 * Request Parameter 들을 로깅을 위해 한줄로 변환하는 메소드 
	 * @return Request Parameter 한줄
	 * @author kimgun-yeong
	 */
	private String paramMapToString(Map<String, String[]> paramMap) {
		StringBuilder sb = new StringBuilder();
		paramMap.entrySet().stream().forEach(entry -> {
			sb.append(entry.getKey()+" : ");
			for(int i=0;i <entry.getValue().length;i++) {
				sb.append(entry.getValue()[i] +" ");
			}
			sb.append(", ");
		});
		
		String paramString = sb.toString();
		paramString = paramString.substring(0, paramString.length()-2);
		
		return paramString;
	}
}
