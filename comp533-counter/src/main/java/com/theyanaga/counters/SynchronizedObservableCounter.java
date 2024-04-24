package com.theyanaga.counters;

import com.theyanaga.helpers.Tracer;
import com.theyanaga.observables.RemoteCounter;
import com.theyanaga.observers.Observer;

import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;


public class SynchronizedObservableCounter extends CounterWithTraceAndLock implements RemoteCounter {


  private ProducerConsumerTurn producerConsumerTurn = ProducerConsumerTurn.PRODUCER_TURN;
  private List<Long> threadIds = new ArrayList<>();

  private List<Thread> consumerThreads = new ArrayList<>();
  private List<Thread> producerThreads = new ArrayList<>();

  public List<Thread> getProducerThreads() {
    return this.producerThreads;
  }

  public List<Thread> getConsumerThreads() {
    return this.consumerThreads;
  }

  private void waitForNotification(boolean fromConsumer) throws InterruptedException  {
//    try {
//      System.out.println(RemoteServer.getClientHost());
//    }
//    catch (ServerNotActiveException e) {
//      e.printStackTrace();
//    }
    Thread thread = Thread.currentThread();
    if (!threadIds.contains(thread.getId())) {
      if (fromConsumer) {
        consumerThreads.add(thread);
//        thread.setName("consumer" + (consumerThreads.size() - 1));
      }
      else {
        producerThreads.add(thread);
//        thread.setName("producer" + (producerThreads.size() - 1));
      }
      threadIds.add(thread.getId());
    }
    Tracer.logCurrentThreadIds(this.threadIds);
    synchronized (thread) {
      thread.wait();
      Tracer.logThread(thread);
    }
  }


  @Override
  public int getValue(String callerName) throws InterruptedException {
	  System.out.println(Thread.currentThread() + " " + Thread.currentThread().getName() );
    waitForNotification(true);
    callerName = Thread.currentThread().getName();
    super.traceSynchronizedMethodAttempt(callerName);
    int rv = synchronizedGetValue(callerName);
    super.traceLeftSynchronizedMethod(callerName);
    sleep();
    return rv;
  }

  public synchronized int synchronizedGetValue(String callerName) {
    super.traceEnteredSynchronizedMethod(callerName);
    sleep();
    waitForConsumerTurn(callerName);
    super.waitForRelease();
    int rv = super.getValue();
    producerConsumerTurn = ProducerConsumerTurn.PRODUCER_TURN;
    notifyAll();
    return rv;
  }

  @Override
  public void increment(String callerName) throws InterruptedException {
	  System.out.println(Thread.currentThread() + " " + Thread.currentThread().getName() );

    waitForNotification(false);
    callerName = Thread.currentThread().getName();
    super.traceSynchronizedMethodAttempt(callerName);
    synchronizedIncrement(callerName);
    super.traceLeftSynchronizedMethod(callerName);
  }


  public synchronized void synchronizedIncrement(String callerName) {
    super.traceEnteredSynchronizedMethod(callerName);
    waitForProducerTurn(callerName);
    super.waitForRelease();
    super.increment();
    producerConsumerTurn = ProducerConsumerTurn.CONSUMER_TURN;
    notify(); // Notify or notify all?
  }

  private void sleep() {
//    try {
//     Thread.sleep(2500L);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
  }

//  @Override
  public void setQueueObserver(Observer observer) {
    super.addObserver(observer);
  }

  public void waitForConsumerTurn(String callerName) {
    while (!(producerConsumerTurn == ProducerConsumerTurn.CONSUMER_TURN)) {
      try {
        super.traceEnteredWait(callerName);
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    super.traceLeftWait(callerName);
  }

  public void waitForProducerTurn(String callerName) {
    while (!(producerConsumerTurn == ProducerConsumerTurn.PRODUCER_TURN)) {
      try {
        super.traceEnteredWait(callerName);
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    super.traceLeftWait(callerName);
  }

//  @Override
  public Lock getLock() {
    return null;
  }

//  @Override
  public void release() {
    super.release();
  }

}
