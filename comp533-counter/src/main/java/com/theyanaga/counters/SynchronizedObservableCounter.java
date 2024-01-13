package com.theyanaga.counters;

import com.theyanaga.observers.Observer;
import com.theyanaga.observers.PropertyChange;

import java.util.ArrayList;
import java.util.List;

public class SynchronizedObservableCounter extends ObservableCounter {

  private List<Observer> observers = new ArrayList<>();

  private Turn turn = Turn.PRODUCER_TURN;

  @Override
  public synchronized int getValue() {
    waitForConsumerTurn();
    int rv = super.getValue();
    turn = Turn.PRODUCER_TURN;
    notify();
    return rv;
  }

  @Override
  public synchronized void increment() {
    waitForProducerTurn();
    super.increment();
    turn = Turn.CONSUMER_TURN;
    notify();
  }

  public void waitForConsumerTurn() {
    while (!(turn == Turn.CONSUMER_TURN)) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void waitForProducerTurn() {
    while (!(turn == Turn.PRODUCER_TURN)) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void notifyObservers(PropertyChange propertyChange) {
    observers.forEach(o -> o.sendChange(propertyChange));
  }
}
