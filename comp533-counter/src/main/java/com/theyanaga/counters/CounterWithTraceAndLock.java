package com.theyanaga.counters;

import com.theyanaga.observers.QueueObserver;
import com.theyanaga.synchronization.Blocker;
import com.theyanaga.synchronization.ThreadMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CounterWithTraceAndLock extends DefaultCounter {

	private List<String> threadsInWait = new ArrayList<>();

	private List<String> threadsInUrgent = new ArrayList<>();

	private Lock lock = new ReentrantLock();

	private boolean shouldWait = true;

	private QueueObserver observer;
	private Blocker releaseBlocker = ThreadMapper.getReleaseBlocker();

	public void addObserver(QueueObserver observer) {
		this.observer = observer;
	}

	public void traceSynchronizedMethodAttempt(String callerName) {
		observer.attemptedSynchronizedGetValue(callerName);
	}
	
	public void traceReadyToProceed(String callerName) {
		observer.readyToProceed(callerName);
	}

	public void traceLeftSynchronizedMethod(String callerName) {
		observer.leftSynchronizedGetValue(callerName);
	}

	public void traceEnteredSynchronizedMethod(String callerName) {
		observer.enteredSynchronizedIncrement(callerName);
	}

	public synchronized void traceEnteredWait(String callerName) {
		if (!threadsInWait.contains(callerName)) {
			threadsInWait.add(callerName);
			observer.enteredWait(callerName);
		}
	}

	public synchronized void traceNotifyAll() {
		for (String anId:threadsInWait ) {
			if (!threadsInUrgent.contains(anId)) {
				threadsInUrgent.add(anId);
				observer.leftWait(anId);
			}		
		}
		threadsInWait.clear();
	}
//	public synchronized void traceLeftWait(String callerName) {
//		if (threadsInUrgent.contains(callerName)) {
////			observer.leftWait(callerName);
//			observer.resumedExecutionAfterWait(callerName);
//			threadsInUrgent.remove(callerName);
//		}
//	}
	public synchronized void traceResumedExecutionAfterWait(String callerName) {
		if (threadsInUrgent.contains(callerName)) {
//			observer.leftWait(callerName);
			observer.resumedExecutionAfterWait(callerName);
			threadsInUrgent.remove(callerName);
		}
	}
	

//	public synchronized void traceLeftWait(String callerName) {
//		if (threadsInWait.contains(callerName)) {
//			observer.leftWait(callerName);
//			observer.resumedExecutionAfterWait(callerName);
//			threadsInWait.remove(callerName);
//		}
//	}

//  public void waitForRelease() {
//    lock.lock();
//    while (shouldWait) {
//      sleep();
//    }
//    shouldWait = true;
//    lock.unlock();
//  }

	public void waitForRelease() {
		releaseBlocker.block();
	}

	public void release() {
		if (!releaseBlocker.hasBlocked()) {
			System.out.println("No thread in monitor");
		} else {
			releaseBlocker.unblock();
		}
	}

//  private void sleep() {
//    try {
//      Thread.sleep(500L);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
//  }
}
