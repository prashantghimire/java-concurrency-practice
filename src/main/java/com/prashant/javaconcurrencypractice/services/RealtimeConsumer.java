package com.prashant.javaconcurrencypractice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RealtimeConsumer {
    public void consume() {
        log.info("consuming real-time");
    }
}
