package com.theyanaga.observers;

import com.theyanaga.helpers.Action;
import com.theyanaga.helpers.Methods;
import com.theyanaga.helpers.Tracer;

import java.util.LinkedList;
import java.util.Queue;

public class QueueObserver implements Observer {

  private String executingThread = "None";
  private Queue<String> entryQueue = new LinkedList<>();
  private Queue<String> conditionQueue = new LinkedList<>();
  private Queue<String> urgentQueue = new LinkedList<>();

  public synchronized void changeState(PropertyChange propertyChange) {
    Tracer.log(propertyChange);
    String currentCaller = propertyChange.callerName();
    switch (propertyChange.methodName()) {
      case RESUME_AFTER_WAIT:
        handleResumeExecutionAfterWait(currentCaller);
      case WAIT:
        handleWait(propertyChange, currentCaller);
        break;
      case INCREMENT:
      case GET_VALUE:
        handleSynchronizedMethod(currentCaller, propertyChange, this.entryQueue);
        break;
    }
    printState();
  }

  private void handleResumeExecutionAfterWait(String currentCaller) {
    this.urgentQueue.removeIf(s -> s.equalsIgnoreCase(currentCaller));
    this.executingThread = currentCaller;
  }

  private void handleWait(PropertyChange propertyChange, String currentCaller) {
    if (propertyChange.action() == Action.ENTERED) {
      this.conditionQueue.add(currentCaller);
      this.executingThread = "None";
    } else if (propertyChange.action() == Action.LEFT) {
      this.conditionQueue.removeIf(s -> s.equalsIgnoreCase(currentCaller));
      this.urgentQueue.add(currentCaller);
      this.executingThread = "None";
    }
  }

  private void handleSynchronizedMethod(String currentCaller, PropertyChange propertyChange, Queue<String> queue) {
    if (propertyChange.action() == Action.ATTEMPTED) {
      queue.add(currentCaller);
    } else if (propertyChange.action() == Action.ENTERED) {
      queue.removeIf(s -> s.equalsIgnoreCase(currentCaller));
      this.executingThread = currentCaller;
    } else {
      this.executingThread = "None";
    }
  }

  private void printState() {
    Tracer.logQueueState(entryQueue, conditionQueue, urgentQueue, executingThread);
  }

  @Override
  public void enteredWait(String callerName) {
    changeState(new PropertyChange(callerName, Methods.WAIT, Action.ENTERED));
  }

  @Override
  public void leftWait(String callerName) {
    changeState(new PropertyChange(callerName, Methods.WAIT, Action.LEFT));
  }

  @Override
  public void attemptedSynchronizedGetValue(String callerName) {
    changeState(new PropertyChange(callerName, Methods.GET_VALUE, Action.ATTEMPTED));
  }

  @Override
  public void enteredSynchronizedGetValue(String callerName) {
    changeState(new PropertyChange(callerName, Methods.GET_VALUE, Action.ENTERED));
  }

  @Override
  public void leftSynchronizedGetValue(String callerName) {
    changeState(new PropertyChange(callerName, Methods.GET_VALUE, Action.LEFT));
  }

  @Override
  public void attemptedSynchronizedIncrement(String callerName) {
    changeState(new PropertyChange(callerName, Methods.INCREMENT, Action.ATTEMPTED));
  }

  @Override
  public void enteredSynchronizedIncrement(String callerName) {
    changeState(new PropertyChange(callerName, Methods.INCREMENT, Action.ENTERED));
  }

  @Override
  public void leftSynchronizedIncrement(String callerName) {
    changeState(new PropertyChange(callerName, Methods.INCREMENT, Action.LEFT));
  }

  @Override
  public void resumedExecutionAfterWait(String callerName) {
    changeState(new PropertyChange(callerName, Methods.RESUME_AFTER_WAIT, Action.ENTERED));
  }


}
