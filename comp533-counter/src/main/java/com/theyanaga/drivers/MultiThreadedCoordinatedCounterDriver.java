package com.theyanaga.drivers;

import com.theyanaga.producers.NumbersProducer;
import com.theyanaga.producers.Producer;
import com.theyanaga.counters.Counter;
import com.theyanaga.counters.SynchronizedCounter;
import com.theyanaga.consumers.Consumer;
import com.theyanaga.consumers.NumbersConsumer;

public class MultiThreadedCoordinatedCounterDriver {
    private static final int NUM_INCREMENTS = 6;
    private static final Counter counter = new SynchronizedCounter();
    private static final Producer producer = new NumbersProducer("Producer1", counter, NUM_INCREMENTS);
    private static final Consumer consumer = new NumbersConsumer("Consumer1", counter, NUM_INCREMENTS);

    public static void main(String[] args){
        Thread producerThread = new Thread(producer);
        producerThread.setName("Producer");
        Thread consumerThread = new Thread(consumer);
        consumerThread.setName("Consumer");
        producerThread.start();
        consumerThread.start();
    }


}
