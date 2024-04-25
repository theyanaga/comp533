package com.theyanaga.server;

import com.theyanaga.counters.SynchronizedObservableCounter;
import com.theyanaga.drivers.PDUserInputDriver;
import com.theyanaga.drivers.UserInputDriver;
import com.theyanaga.factories.CounterFactory;
import com.theyanaga.observables.RemoteCounter;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class CounterServer {

    public static void main(String[] args) throws RemoteException, FileNotFoundException, InterruptedException {
        final Registry rmiRegistry = LocateRegistry.createRegistry(6000);
        SynchronizedObservableCounter counter = CounterFactory.getCounter();
        UnicastRemoteObject.exportObject((RemoteCounter) counter, 0);
        rmiRegistry.rebind("Counter", counter);


//        UserInputDriver.start("None", counter);
        PDUserInputDriver.start("None", counter);

    }

}
