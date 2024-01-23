package com.theyanaga.factories;

import com.theyanaga.counters.SynchronizedObservableCounter;
import com.theyanaga.observers.QueueObserver;

public class CounterFactory {

  public static SynchronizedObservableCounter counter;

  public static SynchronizedObservableCounter getCounter() {
    if (counter == null) {
      counter = new SynchronizedObservableCounter();
      counter.setQueueObserver(new QueueObserver());
    }
    return counter;
  }
}
