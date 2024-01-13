package com.theyanaga.producers;

import com.theyanaga.counters.Counter;

public class NumbersConsumer implements Consumer{

    private final Counter counter;

    private final int numIncrements;

    public NumbersConsumer(Counter counter, int numIncrements) {
        this.counter = counter;
        this.numIncrements = numIncrements;
    }

    public void run() {
        this.consumeNumbers();
    }

    public void consumeNumbers() {
        for (int i = 0; i < numIncrements; i++) {
            this.counter.getValue();
        }
    }
}
