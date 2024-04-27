package com.theyanaga.input;

import java.util.Scanner;

public class ClientInputProcessor {
	static Scanner scanner =  new Scanner(System.in);
	public static String waitForNextInput(String anId) {
		System.out.println("Please hit return to make thread:" + anId + " make next remote call");
		return scanner.nextLine();
	}
}
