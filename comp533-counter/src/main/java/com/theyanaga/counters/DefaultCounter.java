package com.theyanaga.counters;

import com.theyanaga.helpers.Tracer;

public class DefaultCounter implements Counter {

    private int value;

    public int getValue() {
        Tracer.log("getValue<--" + value);
        return value;
    }

    public void increment() {
        int temp = value;
        temp++;
        value = temp;
        Tracer.log("increment-->" + value);
    }

}
