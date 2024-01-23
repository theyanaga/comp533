package com.theyanaga.drivers;

import com.theyanaga.consumers.ObservableNumbersConsumer;
import com.theyanaga.counters.SynchronizedObservableCounter;
import com.theyanaga.factories.ConsumerFactory;
import com.theyanaga.factories.ProducerFactory;
import com.theyanaga.helpers.Tracer;
import com.theyanaga.observers.Observer;
import com.theyanaga.observers.QueueObserver;
import com.theyanaga.producers.ObservableNumbersProducer;
import com.theyanaga.producers.Producer;

import static java.lang.Thread.sleep;

public class MultipleThreadedCoordinatedObservedCounterDriver {

  public static void main(String[] args) throws InterruptedException {
    ObservableNumbersProducer producer1 = ProducerFactory.getProducer();
    ObservableNumbersProducer producer2 = ProducerFactory.getProducer();
    ObservableNumbersConsumer consumer1 = ConsumerFactory.getConsumer();
    ObservableNumbersConsumer consumer2 = ConsumerFactory.getConsumer();
    Thread producerThread1 = new Thread(producer1);
    producerThread1.setName("Producer1");
    Thread consumerThread1 = new Thread(consumer1);
    consumerThread1.setName("Consumer1");
    Thread producerThread2 = new Thread(producer2);
    producerThread2.setName("Producer2");
    Thread consumerThread2 = new Thread(consumer2);
    Tracer.logTraces();
    producerThread1.start();
    producerThread2.start();
    consumerThread1.start();
    consumerThread2.start();
    sleep(500L);
    for (int i = 0; i < 6; i++) {
      notifyRunnable(producer1);
      notifyRunnable(consumer1);
      notifyRunnable(producer2);
      notifyRunnable(consumer2);
    }

  }

  public static void notifyRunnable(Object o) throws InterruptedException {
    synchronized (o) {
      o.notify();
    }
    Thread.sleep(50L);
  }

}
