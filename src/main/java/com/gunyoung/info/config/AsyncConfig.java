package com.gunyoung.info.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.gunyoung.info.async.AsyncExceptionHandler;

import lombok.RequiredArgsConstructor;

/**
 * 비동기 메소드의 실행을 위한 설정 클래스
 * @author kimgun-yeong
 *
 */
@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {
	
	private static final int CORE_POOL_SIZE = 10;
	private static final int MAX_POOL_SIZE = 20;
	private static final int QUEUE_CAPACITY = 1000;
	
	private AsyncExceptionHandler asyncExceptionHandler;
	
	/**
	 * Reject Task 발생 처리 정책으로 {@link ThreadPoolExecutor.CallerRunsPolicy} 채택
	 * @author kimgun-yeong
	 */
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(CORE_POOL_SIZE);
		executor.setMaxPoolSize(MAX_POOL_SIZE);
		executor.setQueueCapacity(QUEUE_CAPACITY);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setThreadNamePrefix("info-asyncExecutor-");
		executor.initialize();
		return executor;
	}
	
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return asyncExceptionHandler;
	}
}
