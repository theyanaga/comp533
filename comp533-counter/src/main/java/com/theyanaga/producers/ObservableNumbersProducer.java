package com.theyanaga.producers;

import com.theyanaga.counters.SynchronizedObservableCounter;
import com.theyanaga.helpers.Methods;
import com.theyanaga.observables.Observable;
import com.theyanaga.observers.Observer;
import com.theyanaga.observers.PropertyChange;

import java.util.ArrayList;
import java.util.List;

public class ObservableNumbersProducer implements Observable, Runnable {

  private List<Observer> observers = new ArrayList<>();

  private final String name;
  private final SynchronizedObservableCounter counter;

  private final int numIncrements;

  public ObservableNumbersProducer(
      String name, SynchronizedObservableCounter counter, int numIncrements) {
    this.name = name;
    this.counter = counter;
    this.numIncrements = numIncrements;
  }

  public void produceNumbers() throws InterruptedException {
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
      this.notifyObservers(new PropertyChange(this.name, Methods.INCREMENT));
      counter.increment(this.name);
    }
  }

  @Override
  public void run() {
    try {
      this.produceNumbers();
    } catch (InterruptedException e) {
      e.printStackTrace();
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
