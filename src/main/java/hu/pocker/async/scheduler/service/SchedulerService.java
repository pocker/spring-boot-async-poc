package hu.pocker.async.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class SchedulerService {

    private static final int SCHEDULER_RATE = 10_000;

    private List<String> input;

    @Autowired
    private AsyncWorker asyncWorker;

    @PostConstruct
    public void postConstruct(){
        input = IntStream
                .range(0,10).mapToObj(i -> UUID.randomUUID())
                .map(UUID::toString)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Scheduled(fixedRate = SCHEDULER_RATE)
    public void onSchedule(){
        log.info("Scheduler started.");

        var jobs = input.stream()
                .map(asyncWorker::doAsyncWork)
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(jobs).join();

        log.info("Scheduler finished.");
    }
}
