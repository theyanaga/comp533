package com.theyanaga.clients;

import com.theyanaga.observables.RemoteCounter;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ProducerClient {

    public static void main(String[] args)
            throws RemoteException, NotBoundException, InterruptedException {
        final Registry rmiRegistry = LocateRegistry.getRegistry("localhost", 6000);
        final Remote counter = (Remote) rmiRegistry.lookup("Counter");

        RemoteCounter aCounter = (RemoteCounter) counter;

        for (int i = 0; i < 6; i++) {
            System.out.println("Called increment!");
            aCounter.increment("producer");
            System.out.println("Returned from increment!");
        }
    }
}
