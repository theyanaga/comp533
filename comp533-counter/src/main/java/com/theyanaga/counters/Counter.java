package com.theyanaga.counters;

import java.rmi.Remote;

public interface Counter {

   public void increment() throws InterruptedException;

   public int getValue() throws InterruptedException;

}
