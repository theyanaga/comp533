package com.theyanaga.input;

import com.theyanaga.counters.SynchronizedObservableCounter;
import com.theyanaga.factories.QueueObserverFactory;
import com.theyanaga.helpers.Tracer;
import com.theyanaga.observers.QueueObserver;
import com.theyanaga.simulation.Simulation;
import com.theyanaga.synchronization.Blocker;
import com.theyanaga.synchronization.ThreadMapper;

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
	  System.out.println("Ready to Proceed:" + aReadyToProceedList);
	  System.out.println("Entry List:" + anEntryQueue);
	  System.out.println("Urgent List:" + anUrgentQueue);
	  System.out.println("Condition List:" + aConditionQueue);
	  ThreadMapper.printMonitorOccupent();	  
  }
  
  public static void printReadyToProceedList(QueueObserver aQueueObserver) {
	  List<String> aReadyToProceedList = aQueueObserver.getReadyToProceedList();
	  System.out.println("Ready to proceed:" + aReadyToProceedList);
  }

  public static void printCurrentOrders(QueueObserver aQueueObserver) {
      System.out.println("Entry Order:" + aQueueObserver.getEntryQueueEnterOrder());
      System.out.println("Exit Order:" + aQueueObserver.getEntryQueueExitOrder());
      System.out.println("Condition Order:" + aQueueObserver.getConditionQueueEntryOrder());
      System.out.println("Condition Exit Order:" + aQueueObserver.getConditionQueueExitOrder());
      System.out.println("Urgent Order:" + aQueueObserver.getUrgentQueueEntryOrder());
      System.out.println("Urgent Exit Order:" + aQueueObserver.getUrgentQueueExitOrder());
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
    
    
    System.out.println("Start controlling your threads!");
    while (true) {
    	System.out.println ("Please enter one of  the following commands:" + Arrays.toString(commands));
      String[] inputs = scanner.nextLine().toLowerCase().split(" ");
      String aCommand = inputs[0].strip();
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
        System.out.println("Quitting program!");
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
//    		  System.out.println("Missing argument to " + ENTER + " command");
//    		  continue;
//    	  }
    	  String aRole = getArgument(inputs);
    	  if (aRole == null) {
    		  System.out.println("Please give client thread argument to command");
    		  continue;
    	  }
    			
//    	  Blocker aBlocker = ThreadMapper.getRoleToBlocker().get(aRole);
//    	  if (aBlocker == null) {
//    		  System.out.println("Unknoewn thread  " + aRole );
//    		  continue;
//    	  }
    	  simulation.unblockThread(aRole);
    		  
    	  
      } else if (DELAY.startsWith(aCommand)) {
//    	  if (inputs.length < 2) {
//    		  System.out.println("Missing argument to " + DELAY + " command");
//    		  continue;
//    	  };
    	  String aDelay = getArgument(inputs);
    	  if (aDelay != null) {
    		  int aSleepAmount = Integer.parseInt(aDelay);
    		  Thread.sleep(aSleepAmount);
    	  }
    	  
      }
        else {
          System.out.println("Invalid command!");
          continue;
        }
    }
    System.exit(0);
  }

  private static String getArgument(String[] strings) {
	  if (strings.length < 2) {
		  System.out.println("Missing argument to command: " + strings[0]);
		  return null;
	  }
	  return strings[1].strip();
  }

  private static Simulation createSimulation(Scanner scanner, SynchronizedObservableCounter counter) {
//    System.out.println("Type a single digit for the number of consumers:");
//    int numConsumers = Integer.parseInt(scanner.nextLine());
//    Tracer.writeCommand(numConsumers + "\n");
//    System.out.printf("There are %d consumers.%n", numConsumers);
//    System.out.println("Type a single digit for the number of producers:");
//    int numProducers= Integer.parseInt(scanner.nextLine());
//    Tracer.writeCommand(numProducers+ "\n");
//    System.out.printf("There are %d producers.%n", numProducers);
//    System.out.printf("Start your consumers and producers.%n");
    System.out.println("Start your consumers and producer clients and type " + DONE + " when they have all been started");

    Tracer.logTraces();
    String input = scanner.nextLine();

    if (input.equalsIgnoreCase("done")) {
      return new Simulation(counter);
    }
    else {
      throw new RuntimeException("Error!");
    }
  }


}
