package com.prashant.javaconcurrencypractice.consumers;

import com.prashant.javaconcurrencypractice.services.Helper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaException;

import java.time.Duration;
import java.util.Collections;

@Slf4j
public class RealtimeConsumerThread extends Thread {
    @Override
    public void run() {
        KafkaConsumer<String, String> realtimeConsumer = new KafkaConsumer<>(Helper.getDefaultKafkaConfig());
        long currentMillis = System.currentTimeMillis();
        log.info("started time={}, will stop after={}", currentMillis, currentMillis + (60*1000));
        try (realtimeConsumer) {
            realtimeConsumer.subscribe(Collections.singleton("customers"));
            log.info("starting consuming batch");
            while (!ConsumerState.errorOccurred.get()) {
                long current = System.currentTimeMillis();
                log.info("current-time={}", current);
                if (current >= currentMillis + (60 * 1000)){
                    log.info("stop time={}", current);
                    throw new KafkaException("something happened in kafka");
                }
                ConsumerRecords<String, String> records = realtimeConsumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    log.info("customers record={}", record);
                }
            }
        } catch (Exception e) {
            ConsumerState.errorOccurred.set(true);
            log.error("error occurred realtime", e);
        } finally {
            log.info("ending consuming realtime");
        }
    }
}
