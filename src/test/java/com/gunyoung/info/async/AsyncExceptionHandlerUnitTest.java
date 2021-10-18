package com.gunyoung.info.async;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
class AsyncExceptionHandlerUnitTest {
	
	@Mock
	Logger logger;
	
	@InjectMocks
	AsyncExceptionHandler asyncExceptionHandler;
	
	/*
	 * public void handleUncaughtException(Throwable ex, Method method, Object... params)
	 */
	
	@Test
	@DisplayName("Uncaught Exception 처리 -> 정상, 로그 내용 확인")
	void handleUncaughtExceptionTest() throws NoSuchMethodException {
		//Given
		Throwable exception = new TestException();
		Method method = Stub.class.getDeclaredMethod("testMethod");
		
		//When
		asyncExceptionHandler.handleUncaughtException(exception, method);
		
		//Then
		then(logger).should(times(1)).error("Uncaught asynchronous exception from: " + method.getDeclaringClass().getName() + "." + method.getName(), exception);
	}
	
	/**
	 * 해당 테스트 클래스에서 활용하기 위한 클래스
	 * @author kimgun-yeong
	 *
	 */
	private static class Stub {
		@SuppressWarnings("unused")
		void testMethod() {
			
		}
	}
	
	/**
	 * 해당 테스트 클래스에서 활용하기 위한 비검사 예외
	 * @author kimgun-yeong
	 *
	 */
	@SuppressWarnings("serial")
	private static class TestException extends RuntimeException {
		
	}
}
