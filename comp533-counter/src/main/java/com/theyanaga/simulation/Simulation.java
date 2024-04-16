package com.theyanaga.simulation;

import com.theyanaga.counters.SynchronizedObservableCounter;
import com.theyanaga.factories.ConsumerFactory;
import com.theyanaga.factories.CounterFactory;
import com.theyanaga.factories.ProducerFactory;
import com.theyanaga.helpers.Tracer;
import com.theyanaga.runnable.ControllableRunnable;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

  private List<ControllableRunnable> consumers = new ArrayList<>();
  private List<ControllableRunnable> producers = new ArrayList<>();

  private List<Thread> threads = new ArrayList<>();

  private long lastExecutedTime;

  public Simulation(int numConsumers, int numProducers) {
    for (int i = 0; i < numConsumers; i++) {
      consumers.add(ConsumerFactory.getConsumer());
    }
    for (int i = 0; i < numProducers; i++) {
      producers.add(ProducerFactory.getProducer());
    }
  }

  public void start() {
    Tracer.logTraces();
    for (ControllableRunnable r : consumers) {
      Thread thread = new Thread(r);
      threads.add(thread);
      thread.start();
    }
    for (ControllableRunnable r : producers) {
      Thread thread = new Thread(r);
      threads.add(thread);
      thread.start();
    }
    this.lastExecutedTime = System.currentTimeMillis();
  }

  public void releaseConsumer(int idx) {
    doDelay();
    Tracer.writeCommand(String.format("c %d%n", idx));
    //    ControllableRunnable consumer = consumers.get(idx);
    //    consumer.setTurn(true);
    //    synchronized (consumer) {
    //      consumer.notify();
    //    }
    Thread thread = threads.get(idx);
    synchronized (thread) {
      thread.notify();
    }
  }

  public void releaseProducer(int idx) {
    doDelay();
    Tracer.writeCommand(String.format("p %d%n", idx));
//    ControllableRunnable producer = producers.get(idx);
//    producer.setTurn(true);
//    synchronized (producer) {
//      producer.notify();
//    }
    Thread thread = threads.get(consumers.size() + idx);
    synchronized (thread) {
      thread.notify();
    }
  }

  public void notifyThreadInMonitor() {
    doDelay();
    Tracer.writeCommand("release\n");
    SynchronizedObservableCounter counter = CounterFactory.getCounter();
    counter.release();
  }

  private void doDelay() {
    long currentTime = System.currentTimeMillis();
    Tracer.writeCommand(String.format("d %d%n", currentTime - lastExecutedTime));
    lastExecutedTime = currentTime;
  }
}
