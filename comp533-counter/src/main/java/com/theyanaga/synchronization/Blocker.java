package com.theyanaga.synchronization;

public interface Blocker {

	boolean hasBlocked();

	Thread getBlockedThread();

	void block();

	void unblock();

}