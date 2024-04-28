package com.theyanaga.grading;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class LogGrader {

	// Tests that Dr.Dewan wants.
	// Entry Queue is a Queue or Not
	// Condition Queue is a Queue or Not
	// Urgent Queue is a Queue or Not
	// Entry -> Condition -> Urgent

	// Ask Dr.Dewan how can we even show that the urgent queue takes priority over
	// the entry, if that's just how it happens.

	public static void main(String[] args) throws FileNotFoundException {
		boolean rv = testThatEntryQueueIsNotQueue("./queueLogs/enter-order.out");
//    boolean rv = testThatUrgentTakesPriorityOverRegularQueue("./queueLogs/monitor-final.out");
		if (rv) {
			System.out.println("Success!");
		} else {
			System.out.println("Error!");
		}
	}

	public static boolean testEntryQueueHasSize(String aFileName, int aSize) throws FileNotFoundException {
		Orders orders = Orders.extraOrdersFromFile(aFileName);
		int aMaxSize = findSizeOfMaximumQueueBuildup(orders.getMonitorEnterOrders(), orders.getMonitorExitOrders());
		System.out.println("Maximum Size of Entry List Found:" + aMaxSize);
		return aMaxSize >= aSize;
	}

	public static boolean testConditionQueueHasSize(String aFileName, int aSize) throws FileNotFoundException {
		Orders orders = Orders.extraOrdersFromFile(aFileName);
		int aMaxSize = findSizeOfMaximumQueueBuildup(orders.getConditionEnterOrders(), orders.getConditionExitOrders());
		System.out.println("Maximum Size of Condition List Found:" + aMaxSize);
		return aMaxSize >= aSize;
	}
	
	public static boolean testUrgentQueueHasSize(String aFileName, int aSize) throws FileNotFoundException {
		Orders orders = Orders.extraOrdersFromFile(aFileName);
		int aMaxSize = findSizeOfMaximumQueueBuildup(
				orders.getUrgentEnterOrders(), 
				orders.getUrgentExitOrders());	
		System.out.println("Maximum Size of Urgent List Found:" + aMaxSize);
		return aMaxSize >= aSize;
	}
	
	public static boolean testThreadAssignments(String aFileName) throws FileNotFoundException {
		boolean aClientThreadAssignedMultipleServerThreads = clientTheadAssignedMultipleServerThreads(aFileName);
		boolean aServerThreadAsAssignedMultipleClienyThreads = serverTheadAssignedMultipleClientThreads(aFileName);
		Orders orders = Orders.extraOrdersFromFile(aFileName);
		List<List<String>> entries = orders.getMonitorEnterOrders();
		List<String> lastEntries = entries.get(entries.size() - 1);
		int aP00Separation = maxSeparationOf(lastEntries, "p00");
		int aC00Separation =  maxSeparationOf(lastEntries, "c00");		
		int aC10Separation =  maxSeparationOf(lastEntries, "c10");
		System.out.println("1-N Client Server Mapping:" + aClientThreadAssignedMultipleServerThreads);
		System.out.println("1-N Server Cluent Mapping:" + aServerThreadAsAssignedMultipleClienyThreads);
		System.out.println("p00 separation:" + aP00Separation);
		System.out.println("c00 separation:" + aC00Separation);
		System.out.println("c10 separation:" + aC10Separation);
		return aClientThreadAssignedMultipleServerThreads ||
				aServerThreadAsAssignedMultipleClienyThreads ||
				aP00Separation > 1 ||
				aC00Separation > 1 ||
				aC10Separation > 1;

	}
	public static List<Integer> indicesOf(List<String> aList, String anOccurence) {
		List<Integer> matchingIndices = new ArrayList<>();
		for (int i = 0; i < aList.size(); i++) {
		    String element = aList.get(i);

		    if (anOccurence.equals(element)) {
		        matchingIndices.add(i);
		    }
		}
		return matchingIndices;
	}
	
	public static int  maxSeparationOf(List<String> aList, String aString) {
		List<Integer> anIndices = indicesOf(aList, aString);
		return maxSeparationOfElements(anIndices);
	}
	
	public static int maxSeparationOfElements(List<Integer> aList) {
		int retVal = 0;
		for (int anIndex = 1; anIndex < aList.size(); anIndex++) {
			int aDistance = aList.get(anIndex) - aList.get(anIndex - 1);
			if (aDistance > retVal) {
				retVal = aDistance;
			}
		}
		return retVal;
	}
	
	
	//Client Thread,p00,Thread[RMI TCP Connection(idle),5,],Thread[RMI TCP Connection(14)-192.168.86.35,5,RMI Runtime]

	public static boolean clientTheadAssignedMultipleServerThreads(String fname) throws FileNotFoundException {
		Scanner in = new Scanner(new FileInputStream(fname));
		while (in.hasNext()) {
		String line = in.nextLine();

	      if (line.startsWith("Client Thread,")) {
	    	  return true;
	      }
		}
		return false;
	}
	// duplocate code, should fix at some point
	public static boolean serverTheadAssignedMultipleClientThreads(String fname) throws FileNotFoundException {
		Scanner in = new Scanner(new FileInputStream(fname));
		while (in.hasNext()) {
		String line = in.nextLine();

	      if (line.startsWith("Server Thread,")) {
	    	  return true;
	      }
		}
		return false;
	}
	public static boolean testThatEntryQueueIsNotQueue(String fname) throws FileNotFoundException {
		Orders orders = Orders.extraOrdersFromFile(fname);
		List<String> anEnterOrders = orders.getMonitorEnterOrders().get(orders.getMonitorEnterOrders().size() - 1);
		List<String> anExitOrders = orders.getMonitorExitOrders().get(orders.getMonitorExitOrders().size() - 1);
		System.out.println("Entry list enter orders:" + anEnterOrders);
		System.out.println("Entry list exit orders:" + anExitOrders);

//    return !areListsEqual(
//    		anEnterOrders,
//    		anExitOrders);
		return !sameListOrders(anEnterOrders, anExitOrders);

//    return !areListsEqual(
//    		orders.getMonitorEnterOrders().get(orders.getMonitorEnterOrders().size() - 1), 
//    		orders.getMonitorExitOrders().get(orders.getMonitorExitOrders().size() - 1));

	}

	public static boolean testThatConditionQueueIsAQueue(String fname) throws FileNotFoundException {
		Orders orders = Orders.extraOrdersFromFile(fname);
		List<String> anEnterOrders = orders.getConditionEnterOrders().get(orders.getConditionEnterOrders().size() - 1);
		List<String> anExitOrders = orders.getConditionExitOrders().get(orders.getConditionExitOrders().size() - 1);

		System.out.println("Condition list enter orders:" + anEnterOrders);
		System.out.println("Condition list exit orders:" + anExitOrders);
		return !sameListOrders(
//    		!areListsEqual(
				anEnterOrders, anExitOrders);
//    return areListsEqual(
//    		orders.getConditionEnterOrders().get(orders.getConditionEnterOrders().size() - 1),
//    		orders.getConditionExitOrders().get(orders.getConditionExitOrders().size() - 1));
	}

	public static boolean testUrgentQueuePrecedence(String fileName) throws FileNotFoundException {
		Orders orders = Orders.extraOrdersFromFile(fileName);
		int aStart = findIndexOflMaximumQueueBuildup(orders.getUrgentEnterOrders(), orders.getUrgentExitOrders());
		int anEnd = findIndexOfQueuesEqual(orders.getUrgentEnterOrders(), orders.getUrgentExitOrders(), aStart);
		boolean aNoChange = checkNoChange(aStart, anEnd, orders.getMonitorExitOrders());
		return aNoChange;

	}

	public static boolean testThatUrgentQueueIsAQueue(String fname) throws FileNotFoundException {

		Orders orders = Orders.extraOrdersFromFile(fname);

		List<String> anEnterOrders = orders.getUrgentEnterOrders().get(orders.getMonitorEnterOrders().size() - 1);

		List<String> anExitOrders = orders.getUrgentExitOrders().get(orders.getMonitorExitOrders().size() - 1);
		System.out.println("Urgent list enter orders:" + anEnterOrders);
		System.out.println("Urgent list exit orders:" + anExitOrders);
		return !sameListOrders(
//	    		!areListsEqual(
				anEnterOrders, anExitOrders);
//    return areListsEqual(
//    		orders.getUrgentEnterOrders().get(orders.getMonitorEnterOrders().size() - 1), 
//    		orders.getUrgentExitOrders().get(orders.getMonitorExitOrders().size() - 1));
	}

	public static int numOrders(List<List<String>> anEntryOrders, List<List<String>> anExitOrders) {
		int numEntryOrders = anEntryOrders.size();
		int numExitOrders = anExitOrders.size();
		int numOrders = numEntryOrders;
		if (anEntryOrders.size() != anExitOrders.size()) {
			System.out.println("Entry orders:" + numEntryOrders + " numExitOrders " + anExitOrders);
			numOrders = Math.min(anEntryOrders.size(), anExitOrders.size());
		}
		return numOrders;
	}

	public static int findSizeOfMaximumQueueBuildup(List<List<String>> anEntryOrders, List<List<String>> anExitOrders) {
		int anIndex = findIndexOflMaximumQueueBuildup(anEntryOrders, anExitOrders);
		List<String> anEntryQueue = anEntryOrders.get(anIndex);
		List<String> anExitQueue = anExitOrders.get(anIndex);
		
		int anEntrySize = anEntryOrders.get(anIndex).size();
		int anExitSize = anExitOrders.get(anIndex).size();
//		int aDifference = anEntryOrders.get(anIndex).size() - anExitOrders.get(anIndex).size();
		int aDifference = anEntrySize - anExitSize;

		
		return aDifference;
	}

	public static int findIndexOflMaximumQueueBuildup(List<List<String>> anEntryOrders,
			List<List<String>> anExitOrders) {
		int aThreshold = 0;
		int retIndex = 0;
//	  System.out.println(anEntryOrders);
//	  System.out.println(anExitOrders);
		int numOrders = numOrders(anEntryOrders, anExitOrders);

		for (int anIndex = 0; anIndex < numOrders; anIndex++) {
			int aDifference = anEntryOrders.get(anIndex).size() - anExitOrders.get(anIndex).size();
			if (aDifference > aThreshold) {
				aThreshold = aDifference;
				retIndex = anIndex;
			}
		}
		return retIndex;

	}

	public static int findIndexOfQueuesEqual(List<List<String>> anEntryOrders, List<List<String>> anExitOrders,
			int from) {
		int numOrders = numOrders(anEntryOrders, anExitOrders);
		for (int anIndex = from; anIndex < numOrders; anIndex++) {
			int aDifference = anEntryOrders.get(anIndex).size() - anExitOrders.get(anIndex).size();
			if (aDifference == 0) {
				return anIndex;
			}
		}
		return -1;
	}

	public static boolean checkNoChange(int from, int to, List<List<String>> anOrders) {
		for (int anIndex = from; anIndex < to - 1; anIndex++) {
			List<String> currentOrder = anOrders.get(anIndex);
			List<String> nextOrder = anOrders.get(anIndex + 1);
			if (!areListsEqual(currentOrder, nextOrder)) {
				return false;
			}
		}
		return true;
	}

	// If the entryQueueEntryOrder != entryQueueExitOrder and urgentQueueEntryOrder
	// != urgentQueueExitOrder. then urgentQueueEntry == urgentQueueExit before
	// entryQueueEntry == entryQueueExit
	public static boolean testThatUrgentTakesPriorityOverRegularQueue(String fname) throws FileNotFoundException {
		Orders orders = Orders.extraOrdersFromFile(fname);
		boolean threadWaitingInUrgentQueue = false;
		boolean checkNextThread = false;
		for (int i = 0; i < orders.getMonitorEnterOrders().size(); i++) {
			List<String> urgentEnterOrder = orders.getUrgentEnterOrders().get(i);
			List<String> urgentExitOrder = orders.getUrgentExitOrders().get(i);

			threadWaitingInUrgentQueue = urgentEnterOrder.size() != urgentExitOrder.size();

			if (checkNextThread) {
				if (threadWaitingInUrgentQueue) {
					return false;
				}
				checkNextThread = false;
			}

			if (threadWaitingInUrgentQueue) {
				checkNextThread = true;
			}
		}

		return true;
	}

	private static boolean areListsEqual(List<String> a, List<String> b) {
		if (a.size() != b.size()) {
			System.out.println("Sizes of two lists are not the same; not every thread entered has exited");
			return false;
		}

		for (int i = 0; i < a.size(); i++) {
			boolean equals = a.get(i).equals(b.get(i));
			if (!equals)
				return false;
		}

		return true;
	}

	private static boolean sameListOrders(List<String> a, List<String> b) {
//	    if (a.size() != b.size()) {
//	    	System.out.println("Sizes of two lists are not the same; not every thread entered has exited");
//	    	return false;
//	    }
		int aMinSize = Math.min(a.size(), b.size());

		for (int i = 0; i < aMinSize; i++) {
			boolean equals = a.get(i).equals(b.get(i));
			if (!equals)
				return false;
		}

		return true;
	}
}
