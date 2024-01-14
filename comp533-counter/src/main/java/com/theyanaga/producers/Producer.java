package com.theyanaga.producers;

public interface Producer extends Runnable {

   public void produceNumbers() throws InterruptedException;

}
