package com.theyanaga.clients;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ConsumerClient02 {
	public static void main (String[] args) throws RemoteException, NotBoundException, InterruptedException {
		String[] myArgs = {"c0", "2"};
		ConsumerClient.main(myArgs);
	}
}
