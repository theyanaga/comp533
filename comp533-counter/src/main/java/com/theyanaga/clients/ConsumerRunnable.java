package com.theyanaga.clients;

import com.theyanaga.observables.RemoteCounter;

public class ConsumerRunnable implements Runnable{
	RemoteCounter counter;
	String id;
	
	public ConsumerRunnable(RemoteCounter aCounter, String anId) {
		counter = aCounter;
		id = anId;
	}
	@Override
	public void run() {
		try {
		    while (true) {
//		      System.out.println("Called getValue!");
		    	
		      counter.getValue(id);
		    	
//		      System.out.println("Returned from getValue!");
		    }
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	System.out.println ("quitting");
				System.exit(-1);
//				e.printStackTrace();
			}
		
	}
	

}
