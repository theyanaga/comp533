package com.theyanaga.counters;

import com.theyanaga.observables.Observable;
import com.theyanaga.observers.Observer;
import com.theyanaga.observers.PropertyChange;

import java.util.ArrayList;
import java.util.List;

public class ObservableCounter implements Counter, Observable {

  private List<Observer> observers = new ArrayList<>();

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

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(PropertyChange propertyChange) {
        observers.forEach(o -> o.sendChange(propertyChange));
    }
}
