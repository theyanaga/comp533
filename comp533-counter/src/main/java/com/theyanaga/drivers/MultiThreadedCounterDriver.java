package com.theyanaga.drivers;

import com.theyanaga.consumers.NumbersProducer;
import com.theyanaga.consumers.Producer;
import com.theyanaga.counters.Counter;
import com.theyanaga.counters.ObservableCounter;
import com.theyanaga.producers.Consumer;
import com.theyanaga.producers.NumbersConsumer;

public class MultiThreadedCounterDriver {

    private static final int NUM_INCREMENTS = 6;
    private static final Counter counter = new ObservableCounter();
    private static final Producer producer = new NumbersProducer(counter, NUM_INCREMENTS);
    private static final Consumer consumer = new NumbersConsumer(counter, NUM_INCREMENTS);

   public static void main(String[] args){
       Thread producerThread = new Thread(producer);
       producerThread.setName("Producer");
       Thread consumerThread = new Thread(consumer);
       consumerThread.setName("Consumer");
       producerThread.start();
       consumerThread.start();
   }


}
