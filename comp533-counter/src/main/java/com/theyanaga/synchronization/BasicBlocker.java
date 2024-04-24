package com.theyanaga.synchronization;

public class BasicBlocker implements Blocker {
	private boolean hasBlocked;
	public boolean hasBlocked() {
		return hasBlocked;
	}
	public Thread getBlockedThread() {
		return blockedThread;
	}
	private Thread blockedThread;
	public synchronized void block() {
		hasBlocked = true;
		try {
			blockedThread = Thread.currentThread();
			super.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void unblock() {
		if (hasBlocked) {
			super.notify();
			hasBlocked = false;
		}
	}
}
