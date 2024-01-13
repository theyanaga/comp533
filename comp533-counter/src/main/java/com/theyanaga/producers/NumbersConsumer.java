package com.theyanaga.producers;

import com.theyanaga.counters.Counter;

public class NumbersConsumer implements Consumer{

    private final Counter counter;

    private final int numIncrements;

    public NumbersConsumer(Counter counter, int numIncrements) {
        this.counter = counter;
        this.numIncrements = numIncrements;
    }

    @Override
    public void run() {
        try {
            this.consumeNumbers();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void consumeNumbers() throws InterruptedException {
        for (int i = 0; i < numIncrements; i++) {
            this.counter.getValue();
        }
    }
}
