package com.theyanaga.helpers;

import com.theyanaga.observers.PropertyChange;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Queue;

public class Tracer {

  private static boolean on = false;

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
              + propertyChange.action();
      write(s + "\n");
    }
  }

  public static void logQueueState(
      Queue<String> entryQueue,
      Queue<String> conditionQueue,
      Queue<String> urgentQueue,
      String executingThread) {
    if (on) {
      write(
          "----------------------------------- Step"
              + step
              + "------------------------------------\n");
      write("Entry Queue: " + entryQueue + "\n");
      write("Condition Queue: " + conditionQueue + "\n");
      write("Urgent Queue: " + urgentQueue + "\n");
      write("Executing Thread: " + executingThread + "\n");
      write("-----------------------------------------------------------------------\n");
      step++;
    }
  }

  public static void logTraces() {
    on = true;
  }

  private static void write(String s) {
    System.out.println(s);
    try {
              Files.writeString(Paths.get("/Users/felipeyanaga/UNC/ta/comp533s24/code-assingments/comp533-counter/src/main/resources/QueueHistory/logs.txt"), s, StandardOpenOption.APPEND);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
