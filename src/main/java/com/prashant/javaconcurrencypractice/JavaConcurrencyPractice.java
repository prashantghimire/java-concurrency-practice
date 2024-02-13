package com.prashant.javaconcurrencypractice;

import com.prashant.javaconcurrencypractice.consumers.BatchConsumerThread;
import com.prashant.javaconcurrencypractice.consumers.ConsumerState;
import com.prashant.javaconcurrencypractice.consumers.RealtimeConsumerThread;
import com.prashant.javaconcurrencypractice.services.BatchConsumer;
import com.prashant.javaconcurrencypractice.services.RealtimeConsumer;
import com.prashant.javaconcurrencypractice.services.StateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class JavaConcurrencyPractice implements ApplicationRunner {
    private final BatchConsumer batchConsumer;
    private final RealtimeConsumer realtimeConsumer;

    private final StateHelper stateHelper;

    public JavaConcurrencyPractice(BatchConsumer batchConsumer, RealtimeConsumer realtimeConsumer, StateHelper stateHelper) {
        this.batchConsumer = batchConsumer;
        this.realtimeConsumer = realtimeConsumer;
        this.stateHelper = stateHelper;
    }

    public static void main(String[] args) {
        SpringApplication.run(JavaConcurrencyPractice.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {

        Thread realTimeThread = new RealtimeConsumerThread();
        Thread batchThread = new BatchConsumerThread();

        realTimeThread.start();
        batchThread.start();

        try {
            batchThread.join();
            realTimeThread.join();
        } catch (Exception e) {
            log.error("error on join", e);
        } finally {
            if (ConsumerState.errorOccurred.get()) {
                System.exit(1);
            }
        }
    }
}
