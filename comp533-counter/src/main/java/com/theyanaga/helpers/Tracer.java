package com.theyanaga.helpers;

import com.theyanaga.observers.PropertyChange;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;
import java.util.Queue;

public class Tracer {

  private static boolean on = false;

  private static final int fileSuffix = new File("/Users/felipeyanaga/UNC/ta/comp533s24/code-assingments/comp533-counter/src/main/resources/QueueHistory/").list().length + 1;

  private static int step = 0;

  public static void log(String s){
    if (on) {
      System.out.println(s);
      write(s + "\n");
    }
  }

  public static void log(PropertyChange propertyChange) {
    if (on) {
      String s =
          propertyChange.callerName()
              + ":"
              + propertyChange.methodName()
              + '-'
              + propertyChange.action()
              + "- TIME - "
              + System.currentTimeMillis() + "\n";
      write(s + "\n");
    }
  }

  public static void logQueueState(
      Queue<String> entryQueue,
      Queue<String> conditionQueue,
      Queue<String> urgentQueue,
      String executingThread) {
    if (on) {
//      write(
//          "----------------------------------- Step"
//              + step
//              + "------------------------------------\n");
//      write("Entry Queue: " + entryQueue + "\n");
//      write("Condition Queue: " + conditionQueue + "\n");
//      write("Urgent Queue: " + urgentQueue + "\n");
//      write("Executing Thread: " + executingThread + "\n");
//      write("-----------------------------------------------------------------------\n");
//      step++;
    }
  }

  public static void logEnterAndExitOrders(
          List<String> entryQueueEnterOrder,
          List<String> entryQueueExitOrder,
          List<String> conditionQueueEnterOrder,
          List<String> conditionQueueExitOrder,
          List<String> urgentQueueEnterOrder,
          List<String> urgentQueueExitOrder
  ) {
    if (on) {
      write("Start Orders.\n");
      write("-----------------------------------------------------------------------\n");
      write("Entry Queue Enter Order: " + entryQueueEnterOrder + "\n");
      write("Entry Queue Exit Order: " + entryQueueExitOrder + "\n");
      write("Condition Queue Enter Order: " + conditionQueueEnterOrder + "\n");
      write("Condition Queue Exit Order: " + conditionQueueExitOrder + "\n");
      write("Urgent Queue Enter Order: " + urgentQueueEnterOrder + "\n");
      write("Urgent Queue Exit Order: " + urgentQueueExitOrder + "\n");
      write("-----------------------------------------------------------------------\n");
      write("End Orders.\n");
    }
  }

  public static void writeCommand(String command) {
    try {
      Files.writeString(Paths.get("/Users/felipeyanaga/UNC/ta/comp533s24/code-assingments/comp533-counter/src/main/resources/QueueHistory/logs" + (fileSuffix) + ".in"), command, StandardOpenOption.APPEND,StandardOpenOption.CREATE);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void logTraces() {
    on = true;
  }

  private static void write(String s) {
    System.out.println(s);
    try {
      Files.writeString(Paths.get("/Users/felipeyanaga/UNC/ta/comp533s24/code-assingments/comp533-counter/src/main/resources/QueueHistory/logs" + (fileSuffix) + ".out"), s, StandardOpenOption.APPEND,StandardOpenOption.CREATE);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
