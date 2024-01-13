package com.theyanaga.consumers;

import com.theyanaga.counters.Counter;

public class NumbersProducer implements Producer {
    private final Counter counter;

    private final int numIncrements;

    public NumbersProducer(Counter counter, int numIncrements) {
        this.counter = counter;
        this.numIncrements = numIncrements;
    }

    public void produceNumbers() throws InterruptedException{
        for (int i = 0; i < numIncrements;i++) {
            counter.increment();
        }
    }

    public void run() {
        try {
            this.produceNumbers();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
