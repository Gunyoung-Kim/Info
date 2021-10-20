package com.gunyoung.info.async;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
	
	private final Logger logger;

	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		logger.error("Uncaught asynchronous exception from: " + method.getDeclaringClass().getName() + "." + method.getName(), ex);
	}

}
