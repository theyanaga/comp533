package com.theyanaga.input;

import com.theyanaga.counters.SynchronizedObservableCounter;
import com.theyanaga.factories.QueueObserverFactory;
import com.theyanaga.helpers.Tracer;
import com.theyanaga.observers.QueueObserver;
import com.theyanaga.simulation.Simulation;
import com.theyanaga.synchronization.Blocker;
import com.theyanaga.synchronization.ThreadMapper;
import com.theyanga.logSending.MonitorLogSender;
import com.theyanga.logSending.MonitorLogSendingRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class ServerInputProcessor {
   public static final String DONE = "done";
  public static final String QUIT = "bye";
  public static final String RELEASE = "release";
  public static final String ENTER = "enter";
  public static final String DELAY = "delay";
  public static final String THREADS = "threads";
  public static final String QUEUES = "queues";
  public static final String HISTORY = "history";
  public static final String MAPPING = "mapping";
  
  private static String[] commands = {
		  THREADS,
		  ENTER, 
		  RELEASE, 
		  QUEUES,
		  MAPPING,
		  HISTORY, 
		  QUIT};
  private static  QueueObserver queueObserver;
  

  
  public static void printCurrentQueues(QueueObserver aQueueObserver) {
	  List<String> aReadyToProceedList = aQueueObserver.getReadyToProceedList();
	  Queue anEntryQueue = aQueueObserver.getEntryQueue();	  
	  Queue aConditionQueue = aQueueObserver.getConditionQueue();
	  Queue anUrgentQueue = aQueueObserver.getUrgentQueue();
	  println("Ready to Proceed:" + aReadyToProceedList);
	  println("Entry List:" + anEntryQueue);
	  println("Urgent List:" + anUrgentQueue);
	  println("Condition List:" + aConditionQueue);
	  ThreadMapper.printMonitorOccupent();	  
  }
  
  public static void println(String aString) {
	  System.out.println(aString);
	  Tracer.traceIOLine(aString);
  }
  public static void printReadyToProceedList(QueueObserver aQueueObserver) {
	  List<String> aReadyToProceedList = aQueueObserver.getReadyToProceedList();
	  println("Ready to proceed:" + aReadyToProceedList);
  }

  public static void printCurrentOrders(QueueObserver aQueueObserver) {
      println("Entry Order:" + aQueueObserver.getEntryQueueEnterOrder());
      println("Exit Order:" + aQueueObserver.getEntryQueueExitOrder());
      println("Condition Order:" + aQueueObserver.getConditionQueueEntryOrder());
      println("Condition Exit Order:" + aQueueObserver.getConditionQueueExitOrder());
      println("Urgent Order:" + aQueueObserver.getUrgentQueueEntryOrder());
      println("Urgent Exit Order:" + aQueueObserver.getUrgentQueueExitOrder());
      ThreadMapper.printMonitorOccupent();

  }

  public static void start(String fileName, SynchronizedObservableCounter counter) throws InterruptedException, FileNotFoundException {


    Scanner scanner = new Scanner(System.in);
    queueObserver = QueueObserverFactory.getSingeton();

    // Get the number of consumers and producers from the user and create the simulation.
    Simulation simulation = createSimulation(scanner, counter);
    simulation.start();

    // Get user input for the commands.
    // quit - quits the program.
    // release -  tell thread inside the monitor to go.
    // "c x" is telling x consumer to go.
    // "p x" is telling x producer to go.
    // "d xxx" is telling the program to sleep "xxx" time.
    
    
    println("Start controlling your threads!");
    while (true) {
    	println ("Please enter one of  the following commands:" + Arrays.toString(commands));
    	String aFullInput = scanner.nextLine().toLowerCase();
//      String[] inputs = scanner.nextLine().toLowerCase().split(" ");
      String[] inputs = aFullInput.split(" ");

      String aCommand = inputs[0].strip();
      Tracer.traceIOLine(">>" + aFullInput);
      if (THREADS.startsWith(aCommand)) {
//    	  ThreadMapper.printCurrentRoles();
    	  printReadyToProceedList(queueObserver);
//      } else if (MAPPING.startsWith(aCommand)) {
//    	  ThreadMapper.printRoleThreadHistory();
//    	  ThreadMapper.printRoleThreadHistory();
      } else  if (MAPPING.startsWith(aCommand)) {
    	  ThreadMapper.printRoleThreadHistory();
    	  ThreadMapper.printThreadRoleHistory();
      }
      else if (QUEUES.startsWith(aCommand)) {
    	  printCurrentQueues(queueObserver);
      }
      else if (QUIT.startsWith(aCommand)){
          // In the case that they passed the file as a parameter to bye, for example "bye pass.out"
          if (inputs.length == 2) {
              Tracer.setOutputFile(inputs[1].strip());
              
          }
          
//        long aSleepTime = (long) (MonitorLogSendingRunnable.getInstance().getTimeDelay() *1.5);
//        Thread.sleep(aSleepTime);
//        println("Quit program!");
        MonitorLogSendingRunnable.getInstance().endProcess(true);
        MonitorLogSender.sendToServer();
        println("Quitting program!");


        break;
      }
      else if (RELEASE.startsWith(aCommand))  {
          simulation.notifyThreadInMonitor();
        }
      else if (HISTORY.startsWith(aCommand)) {
          printCurrentOrders(queueObserver);
      }
      else if (ENTER.startsWith(aCommand)) {
//    	  if (inputs.length < 2) {
//    		  println("Missing argument to " + ENTER + " command");
//    		  continue;
//    	  }
    	  String aRole = getArgument(inputs);
    	  if (aRole == null) {
    		  println("Please give client thread argument to command");
    		  continue;
    	  }
    			
//    	  Blocker aBlocker = ThreadMapper.getRoleToBlocker().get(aRole);
//    	  if (aBlocker == null) {
//    		  println("Unknoewn thread  " + aRole );
//    		  continue;
//    	  }
    	  simulation.unblockThread(aRole);
    		  
    	  
      } else if (DELAY.startsWith(aCommand)) {
//    	  if (inputs.length < 2) {
//    		  println("Missing argument to " + DELAY + " command");
//    		  continue;
//    	  };
    	  String aDelay = getArgument(inputs);
    	  if (aDelay != null) {
    		  int aSleepAmount = Integer.parseInt(aDelay);
    		  Thread.sleep(aSleepAmount);
    	  }
    	  
      }
        else {
          println("Invalid command!");
          continue;
        }
    }
    System.exit(0);
  }

  private static String getArgument(String[] strings) {
	  if (strings.length < 2) {
		  println("Missing argument to command: " + strings[0]);
		  return null;
	  }
	  return strings[1].strip();
  }

  private static Simulation createSimulation(Scanner scanner, SynchronizedObservableCounter counter) {
//    println("Type a single digit for the number of consumers:");
//    int numConsumers = Integer.parseInt(scanner.nextLine());
//    Tracer.writeCommand(numConsumers + "\n");
//    printf("There are %d consumers.%n", numConsumers);
//    System.out.println("Type a single digit for the number of producers:");
//    int numProducers= Integer.parseInt(scanner.nextLine());
//    Tracer.writeCommand(numProducers+ "\n");
//    System.out.printf("There are %d producers.%n", numProducers);
//    printf("Start your consumers and producers.%n");
    println("Start your consumers and producer clients and type " + DONE + " when they have all been started");

    Tracer.logTraces();
    String input = scanner.nextLine();
    Tracer.traceIOLine(">>" + input);
    if (!input.equalsIgnoreCase("done")) {
    	println("Need to type done before entering your input:" + input + "\nAssuming teh input is done \n Enter it again if it was intended to be a command ");
    }

    return new Simulation(counter);
  
//    if (input.equalsIgnoreCase("done")) {
//      return new Simulation(counter);
//    }
//    else {
//    	System.err.println("Need to type done before entering your input:" + input + "\n  Assuming teh input is done \n Enter it again if it was intended to be a command ");
////      throw new RuntimeException("Error!");
//    }
  }


}
