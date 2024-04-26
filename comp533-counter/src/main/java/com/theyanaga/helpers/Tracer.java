package com.theyanaga.helpers;

import com.theyanaga.observers.PropertyChange;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;
import java.util.Queue;

public class Tracer {

  private static boolean on = false;
  static  String LOGS_DIRECTORY_NAME = "./queueLogs";

  static File logDirectory;

  private static int fileSuffix = 0;

  private static Path commandOutputPath;

  private static Path logOutputPath;

  public static synchronized void logThread(Thread thread) {
    log("Thread: " + thread.getName() + " using threadId: " + thread.getId());
  }

  public synchronized static void log(String s){
    if (on) {
      write(s + "\n");
    }
  }

  public static synchronized void  log(PropertyChange propertyChange) {
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

  public static synchronized void logQueueState(
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

  public synchronized static void logCurrentThreadIds(List<Long> threads) {
    if (on) {
      log("Threads: " + threads);
    }

  }

  public synchronized static void logEnterAndExitOrders(
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

  public synchronized static void writeCommand(String command) {
    try {
      Files.writeString(commandOutputPath, command, StandardOpenOption.APPEND,StandardOpenOption.CREATE);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public synchronized static void logTraces() {
    on = true;
  }

  public synchronized  static void setOutputFile(String fname) {
    Path newLogOutput = Paths.get(logDirectory.getAbsolutePath() + "/" + fname + ".out");
    Path newCommandOutput = Paths.get(logDirectory.getAbsolutePath() + "/" + fname + ".in");

    renameFile(".out", newLogOutput);
    renameFile(".in", newCommandOutput);
  }

  private static void renameFile(String x, Path logOutput) {
    File logOutputFile = new File(logDirectory.getAbsolutePath() + "/queues" + fileSuffix + x);
    logOutputFile.renameTo(new File(logOutput.toString()));
  }

  private synchronized static void write(String s) {
    try {
      Files.writeString(logOutputPath, s, StandardOpenOption.APPEND,StandardOpenOption.CREATE);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static {
	  logDirectory = new File(LOGS_DIRECTORY_NAME);
      if (!logDirectory.exists()) {
        logDirectory.mkdirs();
      }
      fileSuffix = (logDirectory.list().length + 2) / 2;
    logOutputPath = Paths.get(logDirectory.getAbsolutePath() + "/queues" + fileSuffix + ".out");
    commandOutputPath = Paths.get(logDirectory.getAbsolutePath() + "/queues" + fileSuffix + ".in");
  }
}
