package com.theyanaga.observables;

import com.theyanaga.observers.Observer;
import com.theyanaga.observers.PropertyChange;
import com.theyanaga.observers.QueueObserver;

public interface Observable {

    public void setQueueObserver(Observer observer);

}
