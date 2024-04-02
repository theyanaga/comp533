package com.theyanaga.drivers;

import com.theyanaga.consumers.ObservableNumbersConsumer;
import com.theyanaga.counters.SynchronizedObservableCounter;
import com.theyanaga.factories.ConsumerFactory;
import com.theyanaga.factories.CounterFactory;
import com.theyanaga.factories.ProducerFactory;
import com.theyanaga.helpers.Tracer;
import com.theyanaga.producers.ObservableNumbersProducer;
import com.theyanaga.runnable.ControllableRunnable;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class MultipleThreadedCoordinatedObservedCounterDriver {

  public static void main(String[] args) throws InterruptedException {
//    regularCase();
//    createConditionQueue();
    System.exit(1);
  }

  private static void createConditionQueue() throws InterruptedException {
    ObservableNumbersProducer producer1 = ProducerFactory.getProducer();
    ObservableNumbersConsumer consumer1 = ConsumerFactory.getConsumer();
    ObservableNumbersConsumer consumer2 = ConsumerFactory.getConsumer();
    Thread producerThread1 = new Thread(producer1);
    Thread consumerThread1 = new Thread(consumer1);
    Thread consumerThread2= new Thread(consumer2);
    Tracer.logTraces();
    consumerThread1.start();
    producerThread1.start();
    consumerThread2.start();
    notifyRunnable(consumer1);
    notifyRunnable(consumer2);
    sleep(500L);
    notifyRunnable(producer1);
    sleep(1000L);
    releaseCounterMonitor();
    sleep(1000L);
    releaseCounterMonitor();
    releaseCounterMonitor();
  }

  private static void regularCase() throws InterruptedException {
    ObservableNumbersProducer producer1 = ProducerFactory.getProducer();
    ObservableNumbersProducer producer2 = ProducerFactory.getProducer();
    ObservableNumbersConsumer consumer1 = ConsumerFactory.getConsumer();
    ObservableNumbersConsumer consumer2 = ConsumerFactory.getConsumer();
    Thread producerThread1 = new Thread(producer1);
    Thread consumerThread1 = new Thread(consumer1);
    Thread producerThread2 = new Thread(producer2);
    Thread consumerThread2 = new Thread(consumer2);
    Tracer.logTraces();
    producerThread1.start();
    producerThread2.start();
    consumerThread1.start();
    consumerThread2.start();
    notifyRunnable(producer1);
    notifyRunnable(consumer1);
    notifyRunnable(producer2);
    notifyRunnable(consumer2);
    releaseCounterMonitor();
    releaseCounterMonitor();
    releaseCounterMonitor();
    releaseCounterMonitor();
  }

  public static void notifyRunnable(ControllableRunnable r) throws InterruptedException {
    r.setTurn(true);
    synchronized (r) {
      r.notify();
    }
  }

  public static void releaseCounterMonitor() throws InterruptedException {
    SynchronizedObservableCounter counter = CounterFactory.getCounter();
    counter.release();
    sleep(500L);
  }

}
