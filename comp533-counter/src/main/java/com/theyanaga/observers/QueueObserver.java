package com.theyanaga.observers;

import com.theyanaga.helpers.Action;
import com.theyanaga.helpers.Methods;
import com.theyanaga.helpers.Tracer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class QueueObserver implements Observer {

  private String executingThread = "None";
  private Queue<String> entryQueue = new LinkedList<>();
  private Queue<String> conditionQueue = new LinkedList<>();
  private Queue<String> urgentQueue = new LinkedList<>();

  private List<String> entryQueueEnterOrder = new ArrayList<>();

  private List<String> entryQueueExitOrder = new ArrayList<>();

  private List<String> conditionQueueEntryOrder = new ArrayList<>();

  private List<String> conditionQueueExitOrder = new ArrayList<>();

  private List<String> urgentQueueEntryOrder = new ArrayList<>();
  private List<String> urgentQueueExitOrder = new ArrayList<>();

  private List<String> monitorEntryOrder = new ArrayList<>();
  private List<String> monitorExitOrder = new ArrayList<>();

  public synchronized void changeState(PropertyChange propertyChange) {
    Tracer.log(propertyChange);
    String currentCaller = propertyChange.callerName();
    switch (propertyChange.methodName()) {
      case RESUME_AFTER_WAIT:
        handleResumeExecutionAfterWait(currentCaller);
        break;
      case WAIT:
        handleWait(propertyChange, currentCaller);
        break;
      case INCREMENT:
      case GET_VALUE:
        handleSynchronizedMethod(currentCaller, propertyChange, this.entryQueue);
        break;
    }
    printState();
    printOrders();
  }

  private void handleResumeExecutionAfterWait(String currentCaller) {
    removeFromQueue(currentCaller, this.urgentQueue, true);
    this.entryQueueExitOrder.add(currentCaller);
    this.executingThread = currentCaller;
  }

  private void handleWait(PropertyChange propertyChange, String currentCaller) {
    if (propertyChange.action() == Action.ENTERED) {
      this.conditionQueue.add(currentCaller);
      this.conditionQueueEntryOrder.add(currentCaller);
      this.executingThread = "None";
    } else if (propertyChange.action() == Action.LEFT) {
      removeFromQueue(currentCaller, conditionQueue, false);
      this.conditionQueueExitOrder.add(currentCaller);
      this.entryQueueEnterOrder.add(currentCaller);
//      this.urgentQueue.add(currentCaller);
//      this.urgentQueueEntryOrder.add(currentCaller);
      this.executingThread = "None";
    }
  }

  private void handleSynchronizedMethod(String currentCaller, PropertyChange propertyChange, Queue<String> queue) {
    if (propertyChange.action() == Action.ATTEMPTED) {
      queue.add(currentCaller);
      entryQueueEnterOrder.add(currentCaller);
    } else if (propertyChange.action() == Action.ENTERED) {
      removeFromQueue(currentCaller, queue, false);
      monitorEntryOrder.add(currentCaller);
      entryQueueExitOrder.add(currentCaller);
      this.executingThread = currentCaller;
    } else {
      this.executingThread = "None";
    }
  }

  private void removeFromQueue(String currentCaller, Queue<String> queue, boolean isUrgentQueue) {
    queue.removeIf(s -> s.equalsIgnoreCase(currentCaller));
  }

  private void printState() {
    Tracer.logQueueState(entryQueue, conditionQueue, urgentQueue, executingThread);
  }

  public void printOrders() {
    Tracer.logEnterAndExitOrders(
        entryQueueEnterOrder,
        entryQueueExitOrder,
        conditionQueueEntryOrder,
        conditionQueueExitOrder,
        urgentQueueEntryOrder,
        urgentQueueExitOrder);
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
