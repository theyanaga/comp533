package com.theyanaga.factories;

import com.theyanaga.counters.SynchronizedObservableCounter;
import com.theyanaga.observers.BasicQueueObserver;

public class CounterFactory {

  public static SynchronizedObservableCounter counter;

  public static SynchronizedObservableCounter getCounter() {
    if (counter == null) {
      counter = new SynchronizedObservableCounter();
//      counter.setQueueObserver(new QueueObserver());
      counter.setQueueObserver(QueueObserverFactory.getSingeton());

    }
    return counter;
  }
}
