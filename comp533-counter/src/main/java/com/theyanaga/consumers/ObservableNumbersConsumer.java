package com.theyanaga.consumers;

import com.theyanaga.counters.SynchronizedObservableCounter;
import com.theyanaga.helpers.Methods;
import com.theyanaga.observables.Observable;
import com.theyanaga.observers.Observer;
import com.theyanaga.observers.PropertyChange;

import java.util.ArrayList;
import java.util.List;

public class ObservableNumbersConsumer implements Observable, Runnable {

  private List<Observer> observers = new ArrayList<>();
  private final String name;

  private final SynchronizedObservableCounter counter;

  private final int numIncrements;

  public ObservableNumbersConsumer(
      String name, SynchronizedObservableCounter counter, int numIncrements) {
    this.name = name;
    this.counter = counter;
    this.numIncrements = numIncrements;
  }

  @Override
  public void run() {
    try {
      this.consumeNumbers();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public synchronized void consumeNumbers() throws InterruptedException {
    for (int i = 0; i < numIncrements; i++) {
      if (i == 0) {
        this.notifyObservers(new PropertyChange(this.name, Methods.INITIAL_WAIT));
      }
      else {
        this.notifyObservers(new PropertyChange(this.name, Methods.WAIT));
      }
      synchronized (this) {
        this.wait();
      }
      this.notifyObservers(new PropertyChange(this.name, Methods.GET_VALUE));
      this.counter.getValue(this.name);
    }
  }

  @Override
  public void addObserver(Observer observer) {
    this.observers.add(observer);
  }

  @Override
  public void notifyObservers(PropertyChange propertyChange) {
    observers.forEach(o -> o.sendChange(propertyChange));
  }
}
