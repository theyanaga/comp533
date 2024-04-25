package com.theyanaga.consumers;

import com.theyanaga.counters.SynchronizedObservableCounter;
import com.theyanaga.helpers.Methods;
import com.theyanaga.observables.Observable;
import com.theyanaga.observers.QueueObserver;
import com.theyanaga.observers.PropertyChange;
import com.theyanaga.runnable.ControllableRunnable;

import java.util.ArrayList;
import java.util.List;

public class ObservableNumbersConsumer implements ControllableRunnable {

  private final String name;

  private final SynchronizedObservableCounter counter;
  private boolean go = false;

  private final int numIncrements;

  public ObservableNumbersConsumer(
      String name, SynchronizedObservableCounter counter, int numIncrements) {
    this.name = name;
    this.counter = counter;
    this.numIncrements = numIncrements;
  }

  @Override
  public void setTurn(boolean val) {
    this.go = val;
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
      this.counter.getValue(this.name);
    }
  }
}
