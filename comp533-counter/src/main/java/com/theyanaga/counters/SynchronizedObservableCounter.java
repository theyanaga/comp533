package com.theyanaga.counters;

import com.theyanaga.observables.ObservableWithLock;
import com.theyanaga.observers.Observer;

import java.util.concurrent.locks.Lock;


public class SynchronizedObservableCounter extends CounterWithTraceAndLock implements ObservableWithLock {

  private ProducerConsumerTurn producerConsumerTurn = ProducerConsumerTurn.PRODUCER_TURN;

  private void waitForNotification() throws InterruptedException {
    Thread thread = Thread.currentThread();
    synchronized (thread) {
      thread.wait();
    }
  }

  public int getValue(String callerName) throws InterruptedException {
    waitForNotification();
    int rv = synchronizedGetValue(callerName);
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

  public void increment(String callerName) throws InterruptedException {
    waitForNotification();
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
    try {
     Thread.sleep(500L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
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

  @Override
  public Lock getLock() {
    return null;
  }

  @Override
  public void release() {
    super.release();
  }

}
