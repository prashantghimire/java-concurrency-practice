package com.prashant.javaconcurrencypractice;

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
    public void run(ApplicationArguments args) throws Exception {

        Thread realTimeThread = new Thread(() -> {
            realtimeConsumer.consume();
        });

        Thread batchThread = new Thread(() -> {
            batchConsumer.consume();
        });

        realTimeThread.start();
        batchThread.start();

        try {
            batchThread.join();
            realTimeThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            batchThread.join();
            realTimeThread.join();
        } finally {
            if (stateHelper.errorOccurred.get()) {
                System.exit(1);
            }
        }

    }
}
