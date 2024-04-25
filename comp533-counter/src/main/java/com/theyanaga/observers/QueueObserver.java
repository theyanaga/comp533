package com.theyanaga.observers;

import java.util.List;
import java.util.Queue;

public interface QueueObserver {


   public void enteredWait(String callerName);

   public void leftWait(String callerName);

   public void attemptedSynchronizedGetValue(String callerName);

   public void enteredSynchronizedGetValue(String callerName);

   public void leftSynchronizedGetValue(String callerName);

   public void attemptedSynchronizedIncrement(String callerName);
   public void enteredSynchronizedIncrement(String callerName);
   public void leftSynchronizedIncrement(String callerName);

  public void resumedExecutionAfterWait(String callerName);

List<String> getUrgentQueueExitOrder();

String getExecutingThread();

Queue<String> getEntryQueue();

Queue<String> getConditionQueue();

Queue<String> getUrgentQueue();

public List<String> getEntryQueueEnterOrder() ;

public List<String> getEntryQueueExitOrder() ;
public List<String> getConditionQueueEntryOrder() ;

public List<String> getConditionQueueExitOrder() ;

List<String> getUrgentQueueEntryOrder() ;

List<String> getMonitorEntryOrder();

List<String> getMonitorExitOrder();
}
