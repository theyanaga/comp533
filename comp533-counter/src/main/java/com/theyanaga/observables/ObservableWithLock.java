package com.theyanaga.observables;

import java.util.concurrent.locks.Lock;

public interface ObservableWithLock extends Observable {

  public Lock getLock();

  public void release();

}
