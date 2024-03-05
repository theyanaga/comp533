package com.theyanaga.counters;

import com.theyanaga.observables.ObservableWithLock;
import com.theyanaga.observers.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SynchronizedObservableCounter extends DefaultCounter implements ObservableWithLock {

  private List<Observer> observers = new ArrayList<>();

  private Lock lock = new ReentrantLock();

  private boolean shouldWait = true;

  private Observer observer;
  private ProducerConsumerTurn producerConsumerTurn = ProducerConsumerTurn.PRODUCER_TURN;

  public int getValue(String callerName) {
    observer.attemptedSynchronizedGetValue(callerName);
    int rv = synchronizedGetValue(callerName);
    observer.leftSynchronizedGetValue(callerName);
    return rv;
  }


  public synchronized int synchronizedGetValue(String callerName) {
    observer.enteredSynchronizedGetValue(callerName);
    sleep();
    waitForConsumerTurn(callerName);
    lock.lock();
    while (shouldWait) {
      sleep();
    }
    shouldWait = true;
    lock.unlock();
    int rv = super.getValue();
    producerConsumerTurn = ProducerConsumerTurn.PRODUCER_TURN;
    notifyAll();
    return rv;
  }

  public void increment(String callerName){
    observer.attemptedSynchronizedIncrement(callerName);
    synchronizedIncrement(callerName);
    observer.leftSynchronizedIncrement(callerName);
  }


  public synchronized void synchronizedIncrement(String callerName) {
    observer.enteredSynchronizedIncrement(callerName);
    waitForProducerTurn(callerName);
    lock.lock();
    while (shouldWait) {
      sleep();
    }
    shouldWait = true;
    lock.unlock();
    super.increment();
    producerConsumerTurn = ProducerConsumerTurn.CONSUMER_TURN;
    notify(); // Notify or notify all?
  }

  private void sleep() {
    try {
     Thread.sleep(50L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setQueueObserver(Observer observer) {
    this.observer = observer;
  }

  public void waitForConsumerTurn(String callerName) {
    while (!(producerConsumerTurn == ProducerConsumerTurn.CONSUMER_TURN)) {
      try {
        observer.enteredWait(callerName);
        wait();
        observer.leftWait(callerName);
        observer.resumedExecutionAfterWait(callerName);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void waitForProducerTurn(String callerName) {
    while (!(producerConsumerTurn == ProducerConsumerTurn.PRODUCER_TURN)) {
      try {
        observer.enteredWait(callerName);
        wait();
        observer.leftWait(callerName);
        observer.resumedExecutionAfterWait(callerName);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public Lock getLock() {
    return null;
  }

  @Override
  public void release() {
    shouldWait = false;
  }

}
