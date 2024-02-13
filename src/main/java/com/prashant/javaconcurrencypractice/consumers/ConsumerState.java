package com.prashant.javaconcurrencypractice.consumers;

import java.util.concurrent.atomic.AtomicBoolean;

public class ConsumerState {
    public static AtomicBoolean errorOccurred = new AtomicBoolean(false);
}
