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

  // Ask Dr.Dewan how can we even show that the urgent queue takes priority over the entry, if that's just how it happens.

  public static void main(String[] args) throws FileNotFoundException {
    boolean rv = testThatEntryQueueIsNotQueue("./queueLogs/enter-order.out");
//    boolean rv = testThatUrgentTakesPriorityOverRegularQueue("./queueLogs/monitor-final.out");
    if (rv) {
      System.out.println("Success!");
    }
    else {
      System.out.println("Error!");
    }
  }

  public static boolean testThatEntryQueueIsNotQueue(String fname) throws FileNotFoundException {
    Orders orders = Orders.extraOrdersFromFile(fname);
    return !areListsEqual(orders.getMonitorEnterOrders().get(orders.getMonitorEnterOrders().size() - 1), orders.getMonitorExitOrders().get(orders.getMonitorExitOrders().size() - 1));
  }

  public static boolean testThatConditionQueueIsAQueue(String fname) throws FileNotFoundException {
    Orders orders = Orders.extraOrdersFromFile(fname);
    return areListsEqual(orders.getConditionEnterOrders().get(orders.getConditionEnterOrders().size() - 1), orders.getConditionExitOrders().get(orders.getConditionExitOrders().size() - 1));
  }

  public static boolean testThatUrgentQueueIsAQueue(String fname) throws FileNotFoundException {
    Orders orders= Orders.extraOrdersFromFile(fname);

    return areListsEqual(orders.getUrgentEnterOrders().get(orders.getMonitorEnterOrders().size() - 1), orders.getUrgentExitOrders().get(orders.getMonitorExitOrders().size() - 1));
  }

  // If the entryQueueEntryOrder != entryQueueExitOrder and urgentQueueEntryOrder != urgentQueueExitOrder. then urgentQueueEntry == urgentQueueExit before entryQueueEntry == entryQueueExit
  public static boolean testThatUrgentTakesPriorityOverRegularQueue(String fname) throws FileNotFoundException {
    Orders orders= Orders.extraOrdersFromFile(fname);
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
    if (a.size() != b.size()) return false;

    for (int i = 0; i < a.size(); i++) {
      boolean equals = a.get(i).equals(b.get(i));
      if (!equals) return false;
    }

    return true;
  }
}
