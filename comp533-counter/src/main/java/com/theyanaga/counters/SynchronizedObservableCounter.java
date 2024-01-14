package com.theyanaga.counters;

import com.theyanaga.helpers.Methods;
import com.theyanaga.observables.Observable;
import com.theyanaga.observers.Observer;
import com.theyanaga.observers.PropertyChange;

import java.util.ArrayList;
import java.util.List;

public class SynchronizedObservableCounter extends DefaultCounter implements Observable {

  private List<Observer> observers = new ArrayList<>();
  private ProducerConsumerTurn producerConsumerTurn = ProducerConsumerTurn.PRODUCER_TURN;

  public synchronized int getValue(String callerName) {
    waitForConsumerTurn(callerName);
    int rv = super.getValue();
    producerConsumerTurn = ProducerConsumerTurn.PRODUCER_TURN;
    this.notifyObservers(new PropertyChange(callerName, Methods.NOTIFY_ALL));
    notifyAll();
    return rv;
  }

  public synchronized void increment(String callerName) {
    waitForProducerTurn(callerName);
    super.increment();
    producerConsumerTurn = ProducerConsumerTurn.CONSUMER_TURN;
    this.notifyObservers(new PropertyChange(callerName, Methods.NOTIFY_ALL));
    notifyAll();
  }

  @Override
  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void notifyObservers(PropertyChange propertyChange) {
    observers.forEach(o -> o.sendChange(propertyChange));
  }

  public void waitForConsumerTurn(String callerName) {
    while (!(producerConsumerTurn == ProducerConsumerTurn.CONSUMER_TURN)) {
      try {
        this.notifyObservers(new PropertyChange(callerName, Methods.WAIT));
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void waitForProducerTurn(String callerName) {
    while (!(producerConsumerTurn == ProducerConsumerTurn.PRODUCER_TURN)) {
      try {
        this.notifyObservers(new PropertyChange(callerName, Methods.WAIT));
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
