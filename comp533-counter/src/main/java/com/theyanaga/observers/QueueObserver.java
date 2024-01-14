package com.theyanaga.observers;

import java.util.LinkedList;
import java.util.Queue;

public class QueueObserver implements Observer {

  private String executingThread = "None";
  private Queue<String> entryQueue = new LinkedList<>();
  private Queue<String> waitingQueue = new LinkedList<>();
  private Queue<String> urgentQueue = new LinkedList<>();

  @Override
  public synchronized void sendChange(PropertyChange propertyChange) {
    wasThreadJustNotified(propertyChange.callerName());
    System.out.println(
        propertyChange.callerName() + " is attempting method: " + propertyChange.methodName());
    switch (propertyChange.methodName()) {
      case INITIAL_WAIT -> this.entryQueue.add(propertyChange.callerName());
      case WAIT -> this.waitingQueue.add(propertyChange.callerName());
      case NOTIFY -> this.handleNotify(propertyChange.callerName());
      case NOTIFY_ALL -> this.handleNotifyAll(propertyChange.callerName());
      default -> {
        executingThread = propertyChange.callerName();
        removeFromQueue(propertyChange.callerName());
      }
    }
    printState();
  }

  private void wasThreadJustNotified(String callerName) {
    if (waitingQueue.contains(callerName)) {
      System.out.println(callerName + " was just notified!");
      waitingQueue.removeIf(s -> s.equalsIgnoreCase(callerName));
      urgentQueue.add(callerName);
      printState();
    }
  }

  private void handleNotifyAll(String callerName) {
  }

  private void handleNotify(String callerName) {
  }

  public void removeFromQueue(String callerName) {
    this.urgentQueue.removeIf(s -> s.equalsIgnoreCase(callerName));
    this.waitingQueue.removeIf(s -> s.equalsIgnoreCase(callerName));
    this.entryQueue.removeIf(s -> s.equalsIgnoreCase(callerName));
  }

  private void printState() {
    System.out.println("Entry Queue: " + entryQueue);
    System.out.println("Waiting Queue: " + waitingQueue);
    System.out.println("Urgent Queue: " + urgentQueue);
    System.out.println("Executing Thread: " + executingThread);
  }
}
