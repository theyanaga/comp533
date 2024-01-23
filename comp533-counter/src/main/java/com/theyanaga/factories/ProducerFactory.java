package com.theyanaga.factories;

import com.theyanaga.counters.SynchronizedObservableCounter;
import com.theyanaga.producers.ObservableNumbersProducer;

public class ProducerFactory {

    public static int nextProducerId = 0;
    public static final int NUM_INCREMENTS = 6;
    public static final SynchronizedObservableCounter counter = CounterFactory.getCounter();

    public static ObservableNumbersProducer getProducer() {
        ObservableNumbersProducer rv = new ObservableNumbersProducer("Producer" + nextProducerId, counter, NUM_INCREMENTS);
        nextProducerId++;
        return rv;
    }

}
