package com.theyanaga.counters;

import com.theyanaga.observables.Observable;
import com.theyanaga.observers.Observer;

import java.util.ArrayList;
import java.util.List;


public class SynchronizedObservableCounter extends DefaultCounter implements Observable {

  private List<Observer> observers = new ArrayList<>();

  private Observer observer;
  private ProducerConsumerTurn producerConsumerTurn = ProducerConsumerTurn.PRODUCER_TURN;

  public int getValue(String callerName) {
    observer.attemptedSynchronizedGetValue(callerName);
    int rv = synchronizedGetValue(callerName);
    observer.leftSynchronizedGetValue(callerName);
    return rv;
  }


  public synchronized int synchronizedGetValue(String callerName) {
    sleep();
    observer.enteredSynchronizedGetValue(callerName);
    waitForConsumerTurn(callerName);
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
    sleep();
    observer.enteredSynchronizedIncrement(callerName);
    waitForProducerTurn(callerName);
    super.increment();
    producerConsumerTurn = ProducerConsumerTurn.CONSUMER_TURN;
    notifyAll(); // Notify or notify all?
  }

  private void sleep() {
    try {
      Thread.sleep(150L);
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

}
