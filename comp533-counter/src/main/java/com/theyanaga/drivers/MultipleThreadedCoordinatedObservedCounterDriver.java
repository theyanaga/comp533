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
  private static final ObservableNumbersProducer producer1 =
      new ObservableNumbersProducer("Producer1",counter, NUM_INCREMENTS);
  private static final ObservableNumbersConsumer consumer1 =
      new ObservableNumbersConsumer("Consumer1", counter, NUM_INCREMENTS);
  private static final ObservableNumbersProducer producer2 =
          new ObservableNumbersProducer("Producer2",counter, NUM_INCREMENTS);
  private static final ObservableNumbersConsumer consumer2 =
          new ObservableNumbersConsumer("Consumer2", counter, NUM_INCREMENTS);

  public static void main(String[] args) throws InterruptedException {
    Thread producerThread1 = new Thread(producer1);
    producerThread1.setName("Producer1");
    Thread consumerThread1 = new Thread(consumer1);
    consumerThread1.setName("Consumer1");
    Thread producerThread2 = new Thread(producer2);
    producerThread2.setName("Producer2");
    Thread consumerThread2 = new Thread(consumer2);
    producer1.addObserver(observer);
    consumer1.addObserver(observer);
    producer2.addObserver(observer);
    consumer2.addObserver(observer);
    counter.addObserver(observer);
    producerThread1.start();
    producerThread2.start();
    consumerThread1.start();
    consumerThread2.start();
    sleep(500L);
    for (int i = 0; i < 6; i++) {
      notifyProducer(producer1);
      notifyConsumer(consumer1);
      notifyProducer(producer2);
      notifyConsumer(consumer2);
    }
  }

  public static void notifyProducer(ObservableNumbersProducer producer) throws InterruptedException {
    synchronized (producer) {
      producer.notify();
      sleep(50L);
    }
  }

  public static void notifyConsumer(ObservableNumbersConsumer consumer) throws InterruptedException {
    synchronized (consumer) {
      consumer.notify();
      sleep(50L);
    }
  }
}
