package com.gunyoung.info.aop;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class LogAdvice {
	private static final Logger logger = LoggerFactory.getLogger(LogAdvice.class);
	
	@Pointcut("within(com.gunyoung.info.controller.*)")
	public void onRequest() {}
	
	@Around("com.gunyoung.info.aop.LogAdvice.onRequest()")
	public Object loggingAroundController(ProceedingJoinPoint pjp) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		Map<String, String[]> paramMap = request.getParameterMap();
		
		String params = "";
		
		if(paramMap.isEmpty() == false) {
			params = "[ " + paramMapToString(paramMap) + "]";
		}
		
		long start = System.currentTimeMillis();
		
		try {
			return pjp.proceed(pjp.getArgs());
		} finally {
			long end = System.currentTimeMillis();
			logger.info("Request: {} {}{} < {} ({}ms)",request.getMethod(), request.getRequestURI(), params, request.getRemoteHost(), end- start);
		}
	}
	
	private String paramMapToString(Map<String, String[]> paramMap) {
		StringBuilder sb = new StringBuilder();
		paramMap.entrySet().stream().forEach(entry -> {
			sb.append(entry.getKey()+" : ");
			for(int i=0;i <entry.getValue().length;i++) {
				sb.append(entry.getValue()[i] +" ");
			}
			sb.append(", ");
		});
		
		return sb.toString();
	}
}
