package com.theyanaga.factories;

import com.theyanaga.consumers.ObservableNumbersConsumer;
import com.theyanaga.counters.SynchronizedObservableCounter;

public class ConsumerFactory {

  public static int nextConsumerId = 0;
  public static final int NUM_INCREMENTS = 6;
  public static final SynchronizedObservableCounter counter = CounterFactory.getCounter();

  public static ObservableNumbersConsumer getConsumer() {
    ObservableNumbersConsumer rv = new ObservableNumbersConsumer("Consumer" + nextConsumerId, counter, NUM_INCREMENTS);
    nextConsumerId++;
    return rv;
  }
}
