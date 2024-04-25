package com.theyanaga.clients;

import com.theyanaga.observables.RemoteCounter;

import java.net.SocketException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConsumerClient {
  static String rootId = "producer";
	static int numThreads = 1;
  public static void main(String[] args)
          throws RemoteException, NotBoundException, InterruptedException {
  	if (args.length >= 1) {
  		rootId = args[0];
  	}
  	if (args.length >= 2) {
  		numThreads = Integer.parseInt(args[1]);
  	}
      final Registry rmiRegistry = LocateRegistry.getRegistry("localhost", 6000);
      final Remote counter = (Remote) rmiRegistry.lookup("Counter");

      RemoteCounter aCounter = (RemoteCounter) counter;
      String mainId = rootId;
      if (numThreads > 1) {
      	mainId = rootId + "0";
      }
      for (int i = 1; i <= numThreads; i++) {
      	String anId = rootId + i;
      	Runnable aForkedRunnable = new ConsumerRunnable(aCounter, anId);
      	new Thread(aForkedRunnable).start();
      }
      Runnable mainRunnable = new ConsumerRunnable(aCounter, mainId);

      mainRunnable.run();
  }
}
