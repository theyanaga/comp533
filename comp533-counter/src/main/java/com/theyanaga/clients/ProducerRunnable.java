package com.theyanaga.clients;

import com.theyanaga.input.ClientInputProcessor;
import com.theyanaga.observables.RemoteCounter;

public class ProducerRunnable implements Runnable{
	RemoteCounter counter;
	String id;
	boolean waitForUserInput;
	
	public ProducerRunnable(RemoteCounter aCounter, String anId, boolean aWaitForUserInput) {
		counter = aCounter;
		id = anId;
		waitForUserInput = aWaitForUserInput;
	}
	@Override
	public void run() {
		try {
		    while (true) {
//		      System.out.println("Called getValue!");
		    	
		      counter.increment(id);
		      if (waitForUserInput) {
		    	  ClientInputProcessor.waitForNextInput(id);
		      }
		    	
//		      System.out.println("Returned from getValue!");
		    }
		    } catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
				System.out.println ("quitting");
				System.exit(-1);
			}
		
	}
	

}
