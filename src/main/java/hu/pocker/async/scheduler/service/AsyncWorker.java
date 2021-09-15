package hu.pocker.async.scheduler.service;

import hu.pocker.async.scheduler.config.AsyncConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AsyncWorker {

    private final Random random = new Random();

    /**
     * This method can't be called internally because the Spring creates a proxy for every managed
     * beans and this proxy will make the call async!
     *
     * We can have multiple thread pool with different behaviours so its always a good thing to indicate which one we would use.
     */
    @Async(AsyncConfig.ASYNC_WORKER_POOL_NAME)
    public CompletableFuture<Void> doAsyncWork(final Object input) {
        log.info("I'm working really hard on the {} input.", input);
        try {
            Thread.sleep(this.getRandomSleepTime());
        } catch (InterruptedException e) {
            /*When the JVM wants to stop the application the Thread.sleep will throw an InterruptedException.
             * In this case we should stop whatever we doing. If we don't do it the app can not stop and the OS or the user eventually
             * will kill it and it can cause data loss.
             * We can check time to time the state of the current thread with Thread.interrupted() and if its true we should save our work and exit.
             * Must be avoid:
             *
             * while(true){
             *  try {
             *   Thread.sleep(1);
             *  } catch (InterruptedException e) {
             *    //NoP
             *  }
             *}
             *
             */
            log.warn("The thread is interrupted.");
        }

        log.info("I'm done with the {} input", input);

        return CompletableFuture.completedFuture(null);
    }

    /**
     * @return random int between 1-10 sec.
     */
    private int getRandomSleepTime(){
        return random.nextInt(9_000) + 1_000;
    }

}
