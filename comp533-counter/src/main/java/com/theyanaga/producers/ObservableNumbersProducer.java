package com.theyanaga.producers;

import com.theyanaga.counters.SynchronizedObservableCounter;
import com.theyanaga.helpers.Methods;
import com.theyanaga.observables.Observable;
import com.theyanaga.observers.Observer;
import com.theyanaga.observers.PropertyChange;
import com.theyanaga.runnable.ControllableRunnable;

import java.util.ArrayList;
import java.util.List;

public class ObservableNumbersProducer implements ControllableRunnable {


  private final String name;
  private final SynchronizedObservableCounter counter;
  private boolean go = false;

  private final int numIncrements;

  public ObservableNumbersProducer(
      String name, SynchronizedObservableCounter counter, int numIncrements) {
    this.name = name;
    this.counter = counter;
    this.numIncrements = numIncrements;
  }

  @Override
  public void setTurn(boolean val) {
    this.go = val;
  }

  public void produceNumbers() throws InterruptedException {
    for (int i = 0; i < numIncrements; i++) {
//      while (!go) {
//        synchronized (this) {
//          this.wait();
//        }
//      }
//      go = false;
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

}
