package com.theyanaga.consumers;

public interface Producer extends Runnable {

   public void produceNumbers() throws InterruptedException;

}
