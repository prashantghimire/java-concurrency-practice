package com.prashant.javaconcurrencypractice;

import com.prashant.javaconcurrencypractice.services.BatchConsumer;
import com.prashant.javaconcurrencypractice.services.RealtimeConsumer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaConcurrencyPractice implements ApplicationRunner {

    private final BatchConsumer batchConsumer;
    private final RealtimeConsumer realtimeConsumer;

    public JavaConcurrencyPractice(BatchConsumer batchConsumer, RealtimeConsumer realtimeConsumer) {
        this.batchConsumer = batchConsumer;
        this.realtimeConsumer = realtimeConsumer;
    }

    public static void main(String[] args) {
        SpringApplication.run(JavaConcurrencyPractice.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        realtimeConsumer.consume();
        batchConsumer.consume();
    }
}
