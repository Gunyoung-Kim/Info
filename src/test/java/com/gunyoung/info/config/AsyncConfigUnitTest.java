package com.gunyoung.info.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.Executor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.gunyoung.info.async.AsyncExceptionHandler;

@ExtendWith(MockitoExtension.class)
class AsyncConfigUnitTest {
	
	@Mock
	AsyncExceptionHandler asyncExceptionHandler;
	
	@InjectMocks
	AsyncConfig asyncConfig;
	
	/*
	 * public Executor getAsyncExecutor() 
	 */
	
	@Test
	@DisplayName("AsyncExecutor 반환 -> 정상, check 구현체 Type")
	void getAsyncExecutorTestCheckType(){
		//Given
		
		//When
		Executor executor = asyncConfig.getAsyncExecutor();
		
		//Then
		assertTrue(executor instanceof ThreadPoolTaskExecutor);
	}
	
	/*
	 * public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler()
	 */
	
	@Test
	@DisplayName("AsyncUncaughtExceptionHandler 반환 -> 정상")
	void getAsyncUncaughtExceptionHandlerTest() {
		//Given
		
		//When
		AsyncUncaughtExceptionHandler result = asyncConfig.getAsyncUncaughtExceptionHandler(); 
		
		//Then
		assertEquals(asyncExceptionHandler, result);
	}
		
}
