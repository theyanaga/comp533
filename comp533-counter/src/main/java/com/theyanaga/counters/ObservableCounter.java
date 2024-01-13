package com.theyanaga.counters;

public class ObservableCounter implements Counter {

    private int value;

    public int getValue() {
        System.out.println("getValue<--" + value);
        return value;
    }

    public void increment() {
        int temp = value;
        temp++;
        value = temp;
        System.out.println("increment-->" + value);
    }

}
