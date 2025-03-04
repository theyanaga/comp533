package com.theyanaga.helpers;

import com.theyanaga.observers.PropertyChange;
import com.theyanga.logSending.MonitorLogSender;

import java.io.File;
import java.io.IOException;
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
  
  private static Path tracePath;

  
//  private static Path latestInputFilePath;
//  private static Path  latestOutputFilePath;

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
//      latestInputFilePath = commandOutputPath;
//      MonitorLogSender.setFileToSend(latestInputFilePath);


    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public synchronized static void traceIOLine(String anIO) {
	    try {
	    	
	      Files.writeString(tracePath, anIO+"\n", StandardOpenOption.APPEND,StandardOpenOption.CREATE);
//	      latestInputFilePath = commandOutputPath;
//	      MonitorLogSender.setFileToSend(latestInputFilePath);
	      MonitorLogSender.setFileToSend(tracePath);


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
    Path newTraceOutput = Paths.get(logDirectory.getAbsolutePath() + "/" + fname + ".trace");
    
//    latestInputFilePath = newCommandOutput;
//    latestOutputFilePath = newLogOutput;
//    MonitorLogSender.setFileToSend(latestInputFilePath);
    
    

    renameFile(".out", newLogOutput);
    renameFile(".in", newCommandOutput);
    renameFile(".trace", newTraceOutput);
    MonitorLogSender.setFileToSend(newTraceOutput);

  }

  private static void renameFile(String x, Path logOutput) {
    File logOutputFile = new File(logDirectory.getAbsolutePath() + "/queues" + fileSuffix + x);
    File aDestinationFile = new File(logOutput.toString());
    if (aDestinationFile.exists()) {
    	aDestinationFile.delete();
    }
//    boolean retVal = logOutputFile.renameTo(new File(logOutput.toString()));
    boolean retVal = logOutputFile.renameTo(aDestinationFile);

    if (!retVal) {
    	System.out.println ("Please manually rename "+ logOutputFile.getAbsolutePath() + " to " + aDestinationFile);
    }
  }

  private synchronized static void write(String s) {
    try {
      Files.writeString(logOutputPath, s, StandardOpenOption.APPEND,StandardOpenOption.CREATE);
//      latestOutputFilePath = logOutputPath;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void clearQueueFiles() {
	  
  }
  public static void createNewFile(Path aPath) {
      File file = aPath.toFile();

      try {
          // Delete the file if it exists
          if (file.exists()) {
              if (file.delete()) {
//                  System.out.println("Existing file " + aPath + " deleted successfully.");
              } else {
                  System.err.println("Failed to delete the existing file.");
                  return; // Exit if deletion fails
              }
          }

          // Create the new file
          if (file.createNewFile()) {
//              System.out.println("New file " + aPath + " created successfully.");
          } else {
              System.err.println("Failed to create the new file.");
          }

      } catch (IOException e) {
          System.err.println("An error occurred: " + e.getMessage());
          e.printStackTrace();
      }
  }

  static {
	  logDirectory = new File(LOGS_DIRECTORY_NAME);
      if (!logDirectory.exists()) {
        logDirectory.mkdirs();
      }
//      fileSuffix = (logDirectory.list().length + 2) / 2;
      fileSuffix = 0;
    logOutputPath = Paths.get(logDirectory.getAbsolutePath() + "/queues" + fileSuffix + ".out");
    commandOutputPath = Paths.get(logDirectory.getAbsolutePath() + "/queues" + fileSuffix + ".in");
    tracePath =  Paths.get(logDirectory.getAbsolutePath() + "/queues" + fileSuffix + ".trace");
    createNewFile(logOutputPath);
    createNewFile(commandOutputPath);
    createNewFile(tracePath);
//    latestInputFilePath = commandOutputPath;
//    latestOutputFilePath = logOutputPath;
    
  }
}
