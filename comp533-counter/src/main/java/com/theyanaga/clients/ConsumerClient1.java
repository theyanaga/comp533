package com.theyanaga.clients;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ConsumerClient1 {
	public static void main (String[] args) throws RemoteException, NotBoundException, InterruptedException {
		String[] myArgs = {"c1"};
		ConsumerClient.main(myArgs);
	}
}
