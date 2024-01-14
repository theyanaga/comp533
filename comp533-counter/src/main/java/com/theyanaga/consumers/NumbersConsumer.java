package com.theyanaga.consumers;

import com.theyanaga.counters.Counter;

public class NumbersConsumer implements Consumer{

    private final String name;

    private final Counter counter;

    private final int numIncrements;

    public NumbersConsumer(String name, Counter counter, int numIncrements) {
        this.name = name;
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
