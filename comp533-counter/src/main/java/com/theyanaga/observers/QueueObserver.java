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
      case WAIT:
        if (propertyChange.action() == Action.ENTERED) {
          this.conditionQueue.add(currentCaller);
          this.executingThread = "None";
        } else if (propertyChange.action() == Action.LEFT) {
          this.conditionQueue.removeIf(s -> s.equalsIgnoreCase(currentCaller));
          this.executingThread = currentCaller;
        }

        break;
      case INCREMENT:
        if (propertyChange.action() == Action.ATTEMPTED) {
          this.entryQueue.add(currentCaller);
        } else if (propertyChange.action() == Action.ENTERED) {
          this.entryQueue.removeIf(s -> s.equalsIgnoreCase(currentCaller));
          this.executingThread = currentCaller;
        } else {
          this.executingThread = "None";
        }
        break;
      case GET_VALUE:
        if (propertyChange.action() == Action.ATTEMPTED) {
          this.entryQueue.add(currentCaller);
        } else if (propertyChange.action() == Action.ENTERED) {
          this.entryQueue.removeIf(s -> s.equalsIgnoreCase(currentCaller));
          this.executingThread = currentCaller;
        } else {
          this.executingThread = "None";
        }
        break;
    }
    printState();
  }

  private void printState() {
    System.out.println("Entry Queue: " + entryQueue);
    System.out.println("Condition Queue: " + conditionQueue);
    System.out.println("Urgent Queue: " + urgentQueue);
    System.out.println("Executing Thread: " + executingThread);
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
}
