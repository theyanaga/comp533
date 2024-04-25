package com.theyanaga.clients;

import com.theyanaga.observables.RemoteCounter;

public class ProducerRunnable implements Runnable{
	RemoteCounter counter;
	String id;
	
	public ProducerRunnable(RemoteCounter aCounter, String anId) {
		counter = aCounter;
		id = anId;
	}
	@Override
	public void run() {
		try {
		    while (true) {
//		      System.out.println("Called getValue!");
		    	
		      counter.increment(id);
		    	
//		      System.out.println("Returned from getValue!");
		    }
		    } catch (Exception e) {
				System.out.println(e);
				System.out.println ("quitting");
				System.exit(-1);
//				e.printStackTrace();
			}
		
	}
	

}
