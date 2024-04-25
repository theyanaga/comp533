package com.theyanaga.clients;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ProducerClient1 {
	public static void main (String[] args) throws RemoteException, NotBoundException, InterruptedException {
		String[] myArgs = {"p1"};
		ProducerClient.main(myArgs);
	}
}
