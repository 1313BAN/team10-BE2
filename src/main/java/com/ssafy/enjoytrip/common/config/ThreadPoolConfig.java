package com.ssafy.enjoytrip.common.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    Executor executor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5); // 기본 스레드 수
		executor.setMaxPoolSize(10); // 최대 스레드 수
		executor.setQueueCapacity(50); // 큐에 대기할 작업 수
		executor.setThreadNamePrefix("api-thread-");
		executor.initialize();
		return executor;
	}
}
