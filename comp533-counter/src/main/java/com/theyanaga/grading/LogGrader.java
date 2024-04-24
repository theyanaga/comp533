package com.theyanaga.grading;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class LogGrader {

  public static void main(String[] args) throws FileNotFoundException {
//    boolean rv =  testThatUrgentQueueExists(
//        "/Users/felipeyanaga/UNC/ta/comp533s24/code-assingments/comp533-counter/src/main/resources/QueueHistory/logs7.out");
    boolean rv =  testThatUrgentQueueExists(
            "queueLogs0.out");
    if (rv) {
      System.out.println("Success!");
    }
    else {
      System.out.println("Error!");
    }
  }

  public static boolean testThatMonitorEnterOrderIsNonDeterministic(String fname) throws FileNotFoundException {
    Scanner in = new Scanner(new FileInputStream(fname));

    List<List<String>> monitorEnterOrders = new ArrayList<>();
    List<List<String>> monitorExitOrders = new ArrayList<>();
    List<List<String>> conditionEnterOrders = new ArrayList<>();
    List<List<String>> conditionExitOrders = new ArrayList<>();
    List<List<String>> urgentEnterOrders = new ArrayList<>();
    List<List<String>> urgentExitOrders = new ArrayList<>();
    while (in.hasNext()) {

      String line = in.nextLine();

      // See if we get the actual queue.
      if (line.equals("Start Orders.")) {
        line =  in.nextLine(); // Skip  the divider
        monitorEnterOrders.add(createListFromLine(in.nextLine()));
        monitorExitOrders.add(createListFromLine(in.nextLine()));
        conditionEnterOrders.add(createListFromLine(in.nextLine()));
        conditionExitOrders.add(createListFromLine(in.nextLine()));
        urgentExitOrders.add(createListFromLine(in.nextLine()));
        urgentEnterOrders.add(createListFromLine(in.nextLine()));
      }
    }

    if (!(conditionEnterOrders.get(0).size() == 0)) {
      return false;
    }

    return !areListsEqual(monitorEnterOrders.get(monitorEnterOrders.size() - 1), monitorExitOrders.get(monitorExitOrders.size() - 1));
  }

  public static boolean testThatUrgentQueueExists(String fname) throws FileNotFoundException {
    Scanner in = new Scanner(new FileInputStream(fname));

    List<List<String>> monitorEnterOrders = new ArrayList<>();
    List<List<String>> monitorExitOrders = new ArrayList<>();
    List<List<String>> conditionEnterOrders = new ArrayList<>();
    List<List<String>> conditionExitOrders = new ArrayList<>();
    List<List<String>> urgentEnterOrders = new ArrayList<>();
    List<List<String>> urgentExitOrders = new ArrayList<>();
    while (in.hasNext()) {

      String line = in.nextLine();

      // See if we get the actual queue.
      if (line.equals("Start Orders.")) {
        line =  in.nextLine(); // Skip  the divider
        monitorEnterOrders.add(createListFromLine(in.nextLine()));
        monitorExitOrders.add(createListFromLine(in.nextLine()));
        conditionEnterOrders.add(createListFromLine(in.nextLine()));
        conditionExitOrders.add(createListFromLine(in.nextLine()));
        urgentExitOrders.add(createListFromLine(in.nextLine()));
        urgentEnterOrders.add(createListFromLine(in.nextLine()));
      }
    }

    // Check if the queue is actually empty.
    if (conditionEnterOrders.get(0).size() == 0) {
      return false;
    }

    return !areListsEqual(monitorEnterOrders.get(monitorEnterOrders.size() - 1), monitorExitOrders.get(monitorExitOrders.size() - 1));
  }

  private static List<String> createListFromLine(String line) {
    String[] nameAndValue = line.split(":");

    String value = nameAndValue[1];
    String[] threadNames = getThreadNames(value);
    return Arrays.asList(threadNames);
//    return Arrays.stream(threadNames).toList();
  }

  private static String[] getThreadNames(String value) {
    return value.trim().replace("[", "").replace("]", "").split(",");
  }

  private static boolean areListsEqual(List<String> a, List<String> b) {
    if (a.size() != b.size()) return false;

    for (int i = 0; i < a.size(); i++) {
      boolean equals = a.get(i).equals(b.get(i));
      if (!equals) return false;
    }

    return true;
  }
}
