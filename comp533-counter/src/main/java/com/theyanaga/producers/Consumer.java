package com.theyanaga.producers;

public interface Consumer extends Runnable {

   public void consumeNumbers() throws InterruptedException;

}
