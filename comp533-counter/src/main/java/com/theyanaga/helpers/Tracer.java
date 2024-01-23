package com.theyanaga.helpers;

import com.theyanaga.observers.PropertyChange;

public class Tracer {

  private static boolean on = false;

  public static void log(PropertyChange propertyChange) {
      if (on) {
          System.out.println(propertyChange.callerName() + ":" + propertyChange.methodName()+ '-' + propertyChange.action());
      }
  }

  public static void logTraces() {
      on = true;
  }
}
