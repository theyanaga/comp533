package com.theyanaga.observers;

public interface Observer {


   public void enteredWait(String callerName);

   public void leftWait(String callerName);

   public void attemptedSynchronizedGetValue(String callerName);

   public void enteredSynchronizedGetValue(String callerName);

   public void leftSynchronizedGetValue(String callerName);

   public void attemptedSynchronizedIncrement(String callerName);
   public void enteredSynchronizedIncrement(String callerName);
   public void leftSynchronizedIncrement(String callerName);

  public void resumedExecutionAfterWait(String callerName);
}
