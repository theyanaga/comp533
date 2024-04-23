package com.theyanaga.counters;

import com.theyanaga.helpers.Tracer;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DefaultCounter implements Counter{

    private int value;

    public int getValue() {
        Tracer.log("getValue<--" + value);
        return value;
    }

    public void increment() {
        int temp = value;
        temp++;
        value = temp;
        Tracer.log("increment-->" + value);
    }

}
