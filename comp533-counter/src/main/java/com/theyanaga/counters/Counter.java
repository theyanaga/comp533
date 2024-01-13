package com.theyanaga.counters;

public interface Counter {

   public void increment() throws InterruptedException;

   public int getValue() throws InterruptedException;

}
