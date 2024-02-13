package com.prashant.javaconcurrencypractice.services;


import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class StateHelper {
    public AtomicBoolean errorOccurred = new AtomicBoolean(false);
}
