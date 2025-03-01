package com.theyanaga.server;

import com.theyanaga.counters.SynchronizedObservableCounter;
import com.theyanaga.factories.CounterFactory;
import com.theyanaga.input.ServerInputProcessor;
import com.theyanaga.input.UserInputDriver;
import com.theyanaga.observables.RemoteCounter;
import com.theyanga.logSending.MonitorLogSendingRunnable;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

// Test that urgent queue is an actual queue by putting the thread in there. c00, c01, then
// c00 will go, c01 will go back to the entry queue, then call a producer, then c01 will go back in, then go c00 again to see what happens.
public class CounterServer {

    public static void main(String[] args) throws RemoteException, FileNotFoundException, InterruptedException {
        final Registry rmiRegistry = LocateRegistry.createRegistry(6000);
        SynchronizedObservableCounter counter = CounterFactory.getCounter();
        UnicastRemoteObject.exportObject((RemoteCounter) counter, 0);
        rmiRegistry.rebind("Counter", counter);
        
        Thread aLogSendingThread = MonitorLogSendingRunnable.runInThread();


        ServerInputProcessor.start("None", counter);

    }

}
