package com.theyanaga.drivers;

import com.theyanaga.consumers.ObservableNumbersConsumer;
import com.theyanaga.counters.SynchronizedObservableCounter;
import com.theyanaga.observers.Observer;
import com.theyanaga.observers.QueueObserver;
import com.theyanaga.producers.ObservableNumbersProducer;

import static java.lang.Thread.sleep;

public class MultipleThreadedCoordinatedObservedCounterDriver {

  private static final int NUM_INCREMENTS = 6;

  private static final Observer observer = new QueueObserver();
  private static final SynchronizedObservableCounter counter = new SynchronizedObservableCounter();
  private static final ObservableNumbersProducer producer =
      new ObservableNumbersProducer("Producer1",counter, NUM_INCREMENTS);
  private static final ObservableNumbersConsumer consumer =
      new ObservableNumbersConsumer("Consumer1", counter, NUM_INCREMENTS);

  public static void main(String[] args) throws InterruptedException {
    Thread producerThread = new Thread(producer);
    producerThread.setName("Producer");
    Thread consumerThread = new Thread(consumer);
    consumerThread.setName("Consumer");
    producer.addObserver(observer);
    consumer.addObserver(observer);
    counter.addObserver(observer);
    producerThread.start();
    consumerThread.start();
    sleep(500L);
    notifyProducer();
    notifyConsumer();
    notifyProducer();
    notifyConsumer();
    notifyProducer();
    notifyConsumer();
    notifyProducer();
    notifyConsumer();
    notifyProducer();
    notifyConsumer();
    notifyProducer();
    notifyConsumer();
  }

  public static void notifyProducer() throws InterruptedException {
    synchronized (producer) {
      producer.notify();
      sleep(50L);
    }
  }

  public static void notifyConsumer() throws InterruptedException {
    synchronized (consumer) {
      consumer.notify();
      sleep(50L);
    }
  }
}
