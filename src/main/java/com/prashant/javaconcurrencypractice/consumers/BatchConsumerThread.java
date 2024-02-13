package com.prashant.javaconcurrencypractice.consumers;

import com.prashant.javaconcurrencypractice.services.Helper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;


@Slf4j
@Service
public class BatchConsumerThread extends Thread {

    @Override
    public void run() {
        KafkaConsumer<String, String> realtimeConsumer = new KafkaConsumer<>(Helper.getDefaultKafkaConfig());
        try (realtimeConsumer) {
            realtimeConsumer.subscribe(Collections.singleton("products"));
            log.info("starting consuming batch");
            while (!ConsumerState.errorOccurred.get()) {
                ConsumerRecords<String, String> records = realtimeConsumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    log.info("product record={}", record);
                }
            }
        } catch (Exception e) {
            ConsumerState.errorOccurred.set(true);
            log.error("error occurred", e);
        } finally {
            log.info("ending consuming batch");
        }
    }
}
