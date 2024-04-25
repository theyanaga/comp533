package com.theyanaga.clients;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ProducerClient03 {
	public static void main (String[] args) throws RemoteException, NotBoundException, InterruptedException {
		String[] myArgs = {"p0", "3"};
		ProducerClient.main(myArgs);
	}
}
