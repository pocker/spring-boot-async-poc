package hu.pocker.async.scheduler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

    public final static String ASYNC_WORKER_POOL_NAME = "AsyncWorkerPool";

    @Bean(ASYNC_WORKER_POOL_NAME)
    public Executor asyncWorkerPool() {
        /*
        * More information about pool management here: https://www.baeldung.com/java-threadpooltaskexecutor-core-vs-max-poolsize
        *  */
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setThreadNamePrefix("AsyncWorker-");
        executor.initialize();
        return executor;
    }
}
