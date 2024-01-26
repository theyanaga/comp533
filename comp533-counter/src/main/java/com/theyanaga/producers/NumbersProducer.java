package com.theyanaga.producers;

import com.theyanaga.counters.Counter;

public class NumbersProducer implements Producer {

    private final String name;
    private final Counter counter;

    private final int numIncrements;

    public NumbersProducer(String name, Counter counter, int numIncrements) {
        this.name = name;
        this.counter = counter;
        this.numIncrements = numIncrements;
    }


    public void produceNumbers() throws InterruptedException{
        for (int i = 0; i < numIncrements;i++) {
                this.wait();
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
