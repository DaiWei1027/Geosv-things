package com.things.common.pool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 通用线程池
 *
 * @author Daiwei
 * @version 1.0.0
 * @date 2022/10/25 10:00
 */
@Configuration
public class CommonPoolTaskExecutor {

    /**
     * 线程池维护线程的最少数量
     */
    private static final int CORE_POOL_SIZE = 50;

    /**
     * 线程池维护线程的最大数量
     */
    private static final int MAX_POOL_SIZE = 200;

    /**
     * 缓存队列
     */
    private static final int QUEUE_CAPACITY = 20;

    /**
     * 允许的空闲时间
     */
    private static final int KEEP_ALIVE = 60;

    @Bean("commonExecutor")
    public ThreadPoolTaskExecutor commonExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix("common_executor-");
        /*
         *  使用此策略，如果添加到线程池失败，那么主线程会自己去执行该任务，不会等待线程池中的线程去执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setKeepAliveSeconds(KEEP_ALIVE);
        executor.initialize();
        return executor;
    }

    @Bean("deviceExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix("device_executor-");
        /*
         *  使用此策略，如果添加到线程池失败，那么主线程会自己去执行该任务，不会等待线程池中的线程去执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setKeepAliveSeconds(KEEP_ALIVE);
        executor.initialize();
        return executor;
    }

    @Bean("ruleExecutor")
    public ThreadPoolTaskExecutor ruleExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix("rule_executor-");
        /*
         *  使用此策略，如果添加到线程池失败，那么主线程会自己去执行该任务，不会等待线程池中的线程去执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setKeepAliveSeconds(KEEP_ALIVE);
        executor.initialize();
        return executor;
    }

    @Bean("actionExecutor")
    public ThreadPoolTaskExecutor actionExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix("rule_executor-");
        /*
         *  使用此策略，如果添加到线程池失败，那么主线程会自己去执行该任务，不会等待线程池中的线程去执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setKeepAliveSeconds(KEEP_ALIVE);
        executor.initialize();
        return executor;
    }
}
