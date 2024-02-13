package com.prashant.javaconcurrencypractice.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@Service
public class RealtimeConsumer {
    
    private final StateHelper stateHelper;

    public RealtimeConsumer(StateHelper stateHelper) {
        this.stateHelper = stateHelper;
    }

    public void consume() {
        KafkaConsumer<String, String> realtimeConsumer = new KafkaConsumer<>(Helper.getDefaultKafkaConfig());
        long currentMillis = System.currentTimeMillis();
        log.info("started time={}", currentMillis);
        try (realtimeConsumer) {
            realtimeConsumer.subscribe(Collections.singleton("customers"));
            log.info("starting consuming real-time");
            while (!stateHelper.errorOccurred.get()) {
                if (System.currentTimeMillis() == currentMillis + (60 * 1000)){
                    log.info("stop time={}", System.currentTimeMillis());
                    throw new KafkaException("something happened in kafka");
                }
                ConsumerRecords<String, String> records = realtimeConsumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    log.info("customer record={}", record);
                }
            }
        }  
        catch (Exception e) {
            stateHelper.errorOccurred.set(true);
            log.error("error occurred", e);
        }
        finally {
            log.info("ending consuming real-time");
            realtimeConsumer.close();
        }
    }
}
