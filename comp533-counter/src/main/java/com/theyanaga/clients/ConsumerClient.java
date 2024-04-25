package com.theyanaga.clients;

import com.theyanaga.observables.RemoteCounter;

import java.net.SocketException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConsumerClient {
  static String consumerId = "consumer";
  public static void main(String[] args)
      throws RemoteException, NotBoundException, InterruptedException {
	  if (args.length >= 1) {
		  consumerId = args[0];
	  }
    final Registry rmiRegistry = LocateRegistry.getRegistry("localhost", 6000);
    final Remote counter = (Remote) rmiRegistry.lookup("Counter");

    RemoteCounter aCounter = (RemoteCounter) counter;

//    for (int i = 0; i < 6; i++) {
    try {
    while (true) {
//      System.out.println("Called getValue!");
    	
      aCounter.getValue(consumerId);
    	
//      System.out.println("Returned from getValue!");
    }
    } catch (Exception e) {
		System.out.println(e);
		System.out.println ("quitting");
		System.exit(-1);
//		e.printStackTrace();
	}
  }
}
