package com.theyanaga.observables;

import com.theyanaga.observers.Observer;
import com.theyanaga.observers.PropertyChange;

public interface Observable {

    public void addObserver(Observer observer);

    public void notifyObservers(PropertyChange propertyChange);
}
