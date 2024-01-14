package com.theyanaga.consumers;

public interface Consumer extends Runnable {

   public void consumeNumbers() throws InterruptedException;

}
