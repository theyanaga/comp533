package com.theyanaga.drivers;

import com.theyanaga.counters.SynchronizedObservableCounter;
import com.theyanaga.helpers.Tracer;
import com.theyanaga.simulation.Simulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class UserInputDriver {

  public static final String QUIT = "quit";
  public static final String RELEASE = "release";

  public static void start(String fileName, SynchronizedObservableCounter counter) throws InterruptedException, FileNotFoundException {


    Scanner scanner;
//    if (args.length > 0) {
//      scanner = new Scanner(new FileInputStream(args[0]));
//    }
//    else {
      scanner = new Scanner(System.in);
//    }


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
      String input = scanner.nextLine();
      if (input.equalsIgnoreCase(QUIT)){
        System.out.println("Quitting program!");
        break;
      }
      else {
        if (input.strip().equalsIgnoreCase(RELEASE))  {
          simulation.notifyThreadInMonitor();
        }
        else if (input.strip().matches("c \\d")) { // TODO: verify this regex
          int idx = getArgument(input);
          simulation.releaseConsumer(idx);
        } else if (input.strip().matches("p \\d")) {
          int idx = getArgument(input);
          simulation.releaseProducer(idx);
        } else if (input.strip().matches("d \\d+")){ // This command should only happen if you are reading from a file!
          int sleepAmount = getArgument(input);
          Thread.sleep(sleepAmount);
        }
        else {
          System.out.println("Invalid command!");
          break;
        }
      }
    }
    System.exit(0);
  }

  private static int getArgument(String input) {
    String[] strings = input.split(" ");
    return Integer.parseInt(strings[1]);
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
    System.out.printf("Start your consumers and producers.%n");
    String input = scanner.nextLine();

    if (input.equalsIgnoreCase("done")) {
      return new Simulation(counter);
    }
    else {
      throw new RuntimeException("Error!");
    }
  }


}
