package com.theyanaga.observables;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteCounter extends Remote {

//  public Lock getLock();
//
//  public void release();

  public int getValue(String callerName) throws InterruptedException, RemoteException;

  public void increment(String callerName) throws InterruptedException, RemoteException;
}
