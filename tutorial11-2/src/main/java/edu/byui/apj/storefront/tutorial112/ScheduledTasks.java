package edu.byui.apj.storefront.tutorial112;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledTasks {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }

    @Scheduled(cron = "0/30 * * * * *")
    public void processNamesInBatches() {
        List<String> names = Arrays.asList(
                "Alice", "Bob", "Charlie", "David", "Emma",
                "Frank", "Grace", "Henry", "Ivy", "Jack",
                "Karen", "Liam", "Mia", "Noah", "Olivia",
                "Paul", "Quinn", "Ryan", "Sophia", "Thomas"
        );

        List<String> batch1 = names.subList(0, names.size() / 2);
        List<String> batch2 = names.subList(names.size() / 2, names.size());

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(() -> processBatch(batch1));
        executorService.execute(() -> processBatch(batch2));

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
            log.info("All done here!");
        } catch (InterruptedException e) {
            log.error("Executor service interrupted", e);
        }
    }


    private void processBatch(List<String> batch) {
        for (String name : batch) {
            log.info("Time: {}, Name: {}", dateTimeFormatter.format(LocalDateTime.now()), name);
            try {
                Thread.sleep(100); // Simulate some processing time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Thread interrupted", e);
            }
        }
    }
}