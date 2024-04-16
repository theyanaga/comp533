package com.theyanaga.counters;

import com.theyanaga.observers.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CounterWithTraceAndLock extends DefaultCounter {

  private List<String> threadsInWait = new ArrayList<>();

  private Lock lock = new ReentrantLock();

  private boolean shouldWait = true;

  private Observer observer;

  public void addObserver(Observer observer) {
    this.observer = observer;
  }


  public void traceSynchronizedMethodAttempt(String callerName) {
    observer.attemptedSynchronizedGetValue(callerName);
  }

  public void traceLeftSynchronizedMethod(String callerName) {
    observer.leftSynchronizedGetValue(callerName);
  }

  public void traceEnteredSynchronizedMethod(String callerName) {
    observer.enteredSynchronizedIncrement(callerName);
  }

  public synchronized void traceEnteredWait(String callerName) {
    if (!threadsInWait.contains(callerName)) {
      threadsInWait.add(callerName);
      observer.enteredWait(callerName);
    }
  }

  public synchronized void traceLeftWait(String callerName) {
    if (threadsInWait.contains(callerName)) {
      observer.leftWait(callerName);
      observer.resumedExecutionAfterWait(callerName);
      threadsInWait.remove(callerName);
    }
  }

  public void waitForRelease() {
    lock.lock();
    while (shouldWait) {
      sleep();
    }
    shouldWait = true;
    lock.unlock();
  }

  public void release() {
    this.shouldWait = false;
  }

  private void sleep() {
    try {
      Thread.sleep(500L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
